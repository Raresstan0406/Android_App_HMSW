package eu.test.healthmonitorsmartwatch

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import eu.test.healthmonitorsmartwatch.ui.theme.AuthViewModel
import eu.test.healthmonitorsmartwatch.ui.theme.BluetoothViewModel
import eu.test.healthmonitorsmartwatch.ui.theme.HealthMonitorSmartwatchTheme
import eu.test.healthmonitorsmartwatch.ui.theme.drawerViewModel
import eu.test.healthmonitorsmartwatch.ui.theme.loginViewModel
import eu.test.healthmonitorsmartwatch.ui.theme.signUpViewModel
import eu.test.healthmonitorsmartwatch.ui.theme.ChatroomViewModel
import eu.test.healthmonitorsmartwatch.ui.theme.MessageViewModel


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val bluetoothManager by lazy {
        applicationContext.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }
    private val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val enableBluetoothLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {}
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { perms ->
            val canEnableBluetooth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                perms[Manifest.permission.BLUETOOTH_CONNECT] == true
            } else true
            if(canEnableBluetooth && !isBluetoothEnabled) {
                enableBluetoothLauncher.launch(
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                )
            }
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                )
            )
        }
        setContent {

            HealthMonitorSmartwatchTheme {
                val viewModel = hiltViewModel<BluetoothViewModel>()
                val state = viewModel.state.collectAsState()
                val viewModelSignUp: signUpViewModel = viewModel()
                val authViewModel: AuthViewModel = viewModel()
                val chatroomViewModel: ChatroomViewModel = viewModel()
                val messageViewModel: MessageViewModel = viewModel()
                val viewModelDrawer: drawerViewModel = viewModel()
                LaunchedEffect(key1 = state.value.errorMessage){
                    state.value.errorMessage?.let {message ->
                        Toast.makeText(
                            applicationContext,
                            message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                LaunchedEffect(key1 = state.value.isConnected){
                    if(state.value.isConnected){
                        Toast.makeText(
                            applicationContext,
                            "You're connected!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when{
                        state.value.isConnecting ->{
                            Column(modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center){
                                CircularProgressIndicator()
                                Text(text = "Conecting..")
                            }

                        }
                        state.value.isConnected ->{
                            appScreenHMSW(//firstName = name,
                                viewModel = viewModelSignUp,
                                drViewModel = viewModelDrawer,
                                blViewModel = viewModel,
                                authViewModel= authViewModel,
                                chatroomViewModel = chatroomViewModel,
                                messageViewModel = messageViewModel
                            )
                        }
                        else ->{
                            HMSW( viewModelSignUp, viewModelDrawer,viewModel, authViewModel, chatroomViewModel, messageViewModel)
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HMSW( viewModelSignUp: signUpViewModel, viewModelDrawer: drawerViewModel, viewModelBluetooth: BluetoothViewModel, authViewModel: AuthViewModel,chatroomViewModel:ChatroomViewModel, messageViewModel: MessageViewModel) {
    val navController = rememberNavController()
    val viewModelLogin: loginViewModel = viewModel()

    NavHost(navController = navController, startDestination = "homescreenHMSW") {
        composable("homescreenHMSW") {
            HomeScreenHMSW(
                navigationToLoginScreen = { navController.navigate("loginscreenHMSW") },
                navigationToAccountTypeScreen = { navController.navigate("signupscreenHMSW") }
            )
        }
        composable("loginscreenHMSW") {
            LoginScreenHMSW(
                navigationToAppScreen = { navController.navigate("appscreenHMSW") },
                viewModel1 = viewModelLogin,
                authViewModel = authViewModel
            )
        }
        composable("signupscreenHMSW") {
            SignUpScreenHMSW(
                navigationToAppScreen = { navController.navigate("appscreenHMSW") },
                viewModel = viewModelSignUp,
                authViewModel = authViewModel
            )
        }
        composable("appscreenHMSW") {
            appScreenHMSW(
                viewModel = viewModelSignUp,
                drViewModel = viewModelDrawer,
                blViewModel = viewModelBluetooth,
                authViewModel = authViewModel,
                chatroomViewModel = chatroomViewModel,
                messageViewModel = messageViewModel
            )
        }
    }
}
