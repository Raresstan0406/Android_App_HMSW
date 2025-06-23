package eu.test.healthmonitorsmartwatch.ui.theme

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.ValueRepository
import data.healthValues

import eu.test.healthmonitorsmartwatch.Graph
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class signUpViewModel(
    private val valuesRepository: ValueRepository = Graph.valuesRepository
) : ViewModel() {
    private val _firstName = mutableStateOf("A")
    private val _lastName = mutableStateOf("A")
    private val _Email = mutableStateOf("A@yahoo.com")
    private val _Password = mutableStateOf("")
    private val _selectedAccountType = mutableStateOf("")

    val firstName: MutableState<String> = _firstName
    val lastName: MutableState<String> = _lastName
    val Password: MutableState<String> = _Password
    val Email: MutableState<String> = _Email
    val selectedAccountType: MutableState<String> = _selectedAccountType
}