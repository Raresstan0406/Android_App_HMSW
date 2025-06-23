package domain.bluetooth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {
    val isConnected: StateFlow<Boolean>
    val scannedDevices: StateFlow<List<BluetoothDevice>>
    val pairedDevices: StateFlow<List<BluetoothDevice>>
    val errors: SharedFlow<String>
    fun startDiscovery()
    fun stopDiscovery()

    suspend fun trySendMessage(message:String): BluetoothMessage?

    fun connectToDevice(device: BluetoothDevice): Flow<ConnectionResult>

    fun closeConnection()

    fun release()
}


//fun startBluetoothServer(): Flow<ConnectionResult>
