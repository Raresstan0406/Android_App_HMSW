package data

import domain.bluetooth.BluetoothMessage


fun String.toBluetoothMessage(isFromLocalUser: Boolean): BluetoothMessage {
    val message = substringAfter(":")
    return BluetoothMessage(
        message = message,
        isFromLocalUser =  isFromLocalUser
    )
}

fun BluetoothMessage.toByteArray(): ByteArray{
    return message.encodeToByteArray()
}