package eu.test.healthmonitorsmartwatch.ui.theme

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import eu.test.healthmonitorsmartwatch.DrScreen

class drawerViewModel: ViewModel() {
    private val _currentScreen: MutableState<DrScreen> = mutableStateOf(DrScreen.DrawerScreen.Home)
    val currentScreen: MutableState<DrScreen>
        get() = _currentScreen

    fun setCurrentScreen(screen: DrScreen){
        _currentScreen.value = screen
    }
}