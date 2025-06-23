package domain.bluetooth

data class BluetoothMessage(
    val message: String,
    val isFromLocalUser: Boolean
)
