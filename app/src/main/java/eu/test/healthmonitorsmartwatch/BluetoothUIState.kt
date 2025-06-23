package eu.test.healthmonitorsmartwatch

import domain.bluetooth.BluetoothDevice
import domain.bluetooth.BluetoothMessage

data class BluetoothUiState(
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevices: List<BluetoothDevice> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
    val messages:List<BluetoothMessage> = emptyList()
)
