package data.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.util.Log
import data.ConnectionState
import data.TempPluseSpO2RecieveManager
import data.TempPulseSpO2Result
import eu.test.healthmonitorsmartwatch.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@SuppressLint("MissingPermission")
class TempPulseSpO2BLERecieveManager @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter,
    private val context: Context
): TempPluseSpO2RecieveManager {

    private val DEVICE_NAME = "ESP32_FINAL_HMSW"
    private val UC_SERVICE_UIID = "4fafc201-1fb5-459e-8fcc-c5c9c331914b"
    private val UC_CHARACTERISTICS_UUID ="beb5483e-36e1-4688-b7f5-ea07361b26a8"


    override val data: MutableSharedFlow<Resource<TempPulseSpO2Result>> = MutableSharedFlow()

    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    private var gatt: BluetoothGatt? = null

    private var isScanning = false

    private val corutineScope = CoroutineScope(Dispatchers.Default)

    private val scanCallback = object :ScanCallback(){
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            if(result?.device?.name ==DEVICE_NAME){
                corutineScope.launch {
                    data.emit(Resource.Loading(message = "Connecting to device..."))
                }
                if(isScanning){
                    result.device.connectGatt(context,false,gattCallback)
                    isScanning =false
                    bleScanner.stopScan(this)
                }
            }
        }
    }

    private var currentConnectionAttempt = 1
    private var MAXIMUM_CONNECTION_ATTEMPTS = 5

    private  val gattCallback = object :BluetoothGattCallback(){
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if(status == BluetoothGatt.GATT_SUCCESS){
                if(newState == BluetoothProfile.STATE_CONNECTED){
                    corutineScope.launch {
                        data.emit(Resource.Loading(message = "Discovering Services..."))
                    }
                    gatt.discoverServices()
                    this@TempPulseSpO2BLERecieveManager.gatt = gatt
                }else if (newState == BluetoothProfile.STATE_DISCONNECTED){
                    corutineScope.launch {
                        data.emit(Resource.Success(data = TempPulseSpO2Result("", connectionState = ConnectionState.Disconnected)))
                    }
                    gatt.close()
                }
            }else{
                gatt.close()
                currentConnectionAttempt+=1
                corutineScope.launch {
                    data.emit(Resource.Loading(message = "Attempting to connect $currentConnectionAttempt/$MAXIMUM_CONNECTION_ATTEMPTS"))
                }
                if(currentConnectionAttempt<=MAXIMUM_CONNECTION_ATTEMPTS){
                    startRecieving()
                }else{
                    corutineScope.launch {
                        data.emit(Resource.Error(errorMessage = "Could not connect to ble device"))
                    }
                }
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            with(characteristic){
                when(uuid){
                    UUID.fromString(UC_CHARACTERISTICS_UUID) -> {
                        val decodedMessage = String(value, Charsets.UTF_8)
                        val message = decodedMessage
                        val tempPulseSpO2Result = TempPulseSpO2Result(
                            message,
                            ConnectionState.Connected
                        )
                        corutineScope.launch {
                            data.emit(
                                Resource.Success(data = tempPulseSpO2Result )
                            )
                        }
                    }
                    else -> Unit
                }
            }
        }

    }

    private fun writeDescription(descriptor: BluetoothGattDescriptor, payload: ByteArray){
        gatt?.let { gatt ->
            descriptor.value = payload
            gatt.writeDescriptor(descriptor)
        } ?: error("Not connected to a BLE device!")
    }


    private fun findCharacteristics(serviceUUID: String, characteristicsUUID:String): BluetoothGattCharacteristic?{
        return gatt?.services?.find { service ->
            service.uuid.toString() == serviceUUID
        }?.characteristics?.find { characteristics ->
            characteristics.uuid.toString() == characteristicsUUID
        }
    }

    override fun reconnect() {
        gatt?.connect()
    }

    override fun disconnect() {
        gatt?.disconnect()
    }

    override fun startRecieving() {
        corutineScope.launch {
            data.emit(Resource.Loading(message = "Scanning BLE devices"))
        }
        isScanning = true
        bleScanner.startScan(null,scanSettings,scanCallback)
    }

    override fun closeConnection() {
        bleScanner.stopScan(scanCallback)
        val characteristic = findCharacteristics(UC_SERVICE_UIID, UC_CHARACTERISTICS_UUID)
        if(characteristic != null){
            disconnectCharacteristic(characteristic)
        }
        gatt?.close()

    }
    private fun disconnectCharacteristic(characteristic: BluetoothGattCharacteristic){
        val cccdUuid = UUID.fromString(CCCD_DESCRIPTOR_UUID)
        characteristic.getDescriptor(cccdUuid)?.let { cccdDescriptor ->
            if(gatt?.setCharacteristicNotification(characteristic,false) == false){
                Log.d("TempPulseSpO2 ReceiveManager","set charateristics notification failed")
                return
            }
            writeDescription(cccdDescriptor, BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE)
        }
    }
}