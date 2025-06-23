package eu.test.healthmonitorsmartwatch

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import eu.test.healthmonitorsmartwatch.ui.theme.AuthViewModel
import eu.test.healthmonitorsmartwatch.ui.theme.BluetoothViewModel
import eu.test.healthmonitorsmartwatch.ui.theme.ChatroomViewModel
import eu.test.healthmonitorsmartwatch.ui.theme.MessageViewModel
import eu.test.healthmonitorsmartwatch.ui.theme.drawerViewModel
import eu.test.healthmonitorsmartwatch.ui.theme.signUpViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun appScreenHMSW(viewModel: signUpViewModel,
                  drViewModel: drawerViewModel,
                  blViewModel: BluetoothViewModel,
                  authViewModel: AuthViewModel,
                  chatroomViewModel: ChatroomViewModel,
                  messageViewModel: MessageViewModel
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope: CoroutineScope = rememberCoroutineScope()
    val controller: NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    if (authViewModel.currentUserAccountType.value == "Pacient") {
        drViewModel.setCurrentScreen(DrScreen.DrawerScreen.Home)
    } else {
        drViewModel.setCurrentScreen(DrScreen.DrawerScreen.Search)
    }
    val currentScreen = remember{
        drViewModel.currentScreen.value
    }
    val title = remember {
        mutableStateOf(currentScreen.title)
    }

    val icon = remember {
        mutableStateOf(currentScreen)
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = title.value) },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Menu"
                        )
                    }
                }
            )
        }, scaffoldState= scaffoldState,
        drawerContent = {
            LazyColumn(Modifier.padding(16.dp)){
                items(getScreensInDrawer(authViewModel)){
                    item ->  
                    DrawerItem(selected = currentRoute == item.dRoute , item = item) {
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                        controller.navigate(item.dRoute)
                        title.value = item.dTitle
                    }
                }
            }
        }
    ) {
        Navigation(navController = controller, viewModelSignUp = viewModel, viewModelBluetooth = blViewModel, authViewModel = authViewModel,chatroomViewModel,messageViewModel,  pd = it)
    }
}

@Composable
fun DrawerItem(
    selected: Boolean,
    item: DrScreen.DrawerScreen,
    onDrawerItemClicked: () -> Unit
    ){
    val background = if (selected) Color.Gray else Color.White
    Row(
        modifier= Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .background(background)
            .clickable {
                onDrawerItemClicked()
            }) {
        Icon(painter = painterResource(id = item.icon),
            contentDescription = item.dTitle,
            Modifier.padding(end= 8.dp, top = 4.dp)
        )
        Text(text = item.dTitle)

    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(navController: NavController, viewModelSignUp: signUpViewModel,viewModelBluetooth: BluetoothViewModel,authViewModel: AuthViewModel, chatroomViewModel: ChatroomViewModel,messageViewModel: MessageViewModel, pd: PaddingValues) {
    val startDestination = if (authViewModel.currentUserAccountType.value == "Pacient") {
        DrScreen.DrawerScreen.Home.dRoute
    } else {
        DrScreen.DrawerScreen.Search.dRoute
    }
    NavHost(
        navController = navController as NavHostController,
        startDestination = startDestination,
        Modifier.padding(pd)
    ) {
        composable(DrScreen.DrawerScreen.Home.dRoute) {
            HomeAppScreenHMSW(viewModelSignUp, authViewModel)
        }
        composable(DrScreen.DrawerScreen.HealthValues.dRoute) {
            val state = viewModelBluetooth.state.collectAsState()
            HealthValuesScreenHMSW(
                state = state.value,
                onStartScan = viewModelBluetooth::startScan,
                deviceConnect = viewModelBluetooth::connectToDevice
            )
        }
        composable(DrScreen.DrawerScreen.Search.dRoute){
            SearchScreenHMSW(authViewModel, chatroomViewModel)
        }
        composable(DrScreen.DrawerScreen.ChatRooms.dRoute){
            ChatroomsScreenHMSW(
                navigationToChatScreen = {roomName -> navController.navigate("chatscreenHMSW/$roomName") },
                authViewModel,
                chatroomViewModel)
        }
        composable("chatscreenHMSW/{roomName}"){
            val roomName = it.arguments?.getString("roomName") ?: ""
            ChatScreen(roomName, messageViewModel)
        }
    }
}
