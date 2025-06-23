package eu.test.healthmonitorsmartwatch.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.AlarmRepository
import data.ConnectionState
import data.TempPluseSpO2RecieveManager
import data.ValueRepository
import data.alarms
import data.healthValues
import eu.test.healthmonitorsmartwatch.Graph
import eu.test.healthmonitorsmartwatch.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TempPulseSpO2ViewModel @Inject constructor(
    private val tempPulseSpO2ReceiveManager: TempPluseSpO2RecieveManager,
) : ViewModel(){

    var initializingMessage by mutableStateOf<String?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var message by mutableStateOf("")
        private set


    var connectionState by mutableStateOf<ConnectionState>(ConnectionState.Uninitialized)

    private fun subscribeToChanges(){
        viewModelScope.launch {
            tempPulseSpO2ReceiveManager.data.collect{ result ->
                when(result){
                    is Resource.Success -> {
                        connectionState = result.data.connectionState
                        message = result.data.message
                    }

                    is Resource.Loading -> {
                        initializingMessage = result.message
                        connectionState = ConnectionState.CurrentlyInitializing
                    }

                    is Resource.Error -> {
                        errorMessage = result.errorMessage
                        connectionState = ConnectionState.Uninitialized
                    }
                }
            }
        }
    }
    fun disconnect(){
        tempPulseSpO2ReceiveManager.disconnect()
    }
    fun reconnect(){
        tempPulseSpO2ReceiveManager.reconnect()
    }
    fun initializeConnection(){
        errorMessage = null
        subscribeToChanges()
        tempPulseSpO2ReceiveManager.startRecieving()
    }
    override fun onCleared() {
        super.onCleared()
        tempPulseSpO2ReceiveManager.closeConnection()
    }
    private val valuesRepository: ValueRepository = Graph.valuesRepository
    lateinit var getAllValues: Flow<List<healthValues>>
    private val alarmsRepository: AlarmRepository = Graph.alarmsRepository
    lateinit var getAllAlarms: Flow<List<alarms>>
    init {
        viewModelScope.launch {
            getAllValues = valuesRepository.getValues()
        }
    }
    fun addValues(values: healthValues){
        viewModelScope.launch(Dispatchers.IO) {
            valuesRepository.addValues(values= values)
        }
    }
    fun deleteAllValues() {
        viewModelScope.launch(Dispatchers.IO) {
            valuesRepository.deleteAllValues()
        }
    }
    init {
        viewModelScope.launch {
            getAllAlarms = alarmsRepository.getAlarms()
        }
    }
    fun addAlarms(alarms: alarms){
        viewModelScope.launch(Dispatchers.IO) {
            alarmsRepository.addAlarms(values= alarms)
        }
    }
    fun deleteAllAlarms() {
        viewModelScope.launch(Dispatchers.IO) {
            alarmsRepository.deleteAllAlarms()
        }
    }
}