package domain.bluetooth

sealed interface ConnectionResult {
    object ConnectionEstablished: ConnectionResult
    data class TransferSucceded(val message: BluetoothMessage): ConnectionResult
    data class Error(val message: String): ConnectionResult
}