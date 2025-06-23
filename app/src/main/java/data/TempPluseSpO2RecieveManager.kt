package data

import eu.test.healthmonitorsmartwatch.Resource
import kotlinx.coroutines.flow.MutableSharedFlow

interface TempPluseSpO2RecieveManager {
    val data:MutableSharedFlow<Resource<TempPulseSpO2Result>>
    fun reconnect()
    fun disconnect()
    fun startRecieving()
    fun closeConnection()
}