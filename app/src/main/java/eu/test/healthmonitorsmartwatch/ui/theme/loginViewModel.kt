package eu.test.healthmonitorsmartwatch.ui.theme

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel

class loginViewModel : ViewModel() {
    private val _Email = mutableStateOf("")
    private val _Password = mutableStateOf("")

    val Email: MutableState<String> = _Email
    val Password: MutableState<String> = _Password
}