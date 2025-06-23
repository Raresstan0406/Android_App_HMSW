package eu.test.healthmonitorsmartwatch

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import data.ConnectionState
import data.alarms
import data.healthValues
import data.parseVitals
import data.validation
import eu.test.healthmonitorsmartwatch.permission.PermissionUtils
import eu.test.healthmonitorsmartwatch.ui.theme.AuthViewModel
import eu.test.healthmonitorsmartwatch.ui.theme.TempPulseSpO2ViewModel
import eu.test.healthmonitorsmartwatch.ui.theme.signUpViewModel
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeAppScreenHMSW(viewModel: signUpViewModel, authViewModel: AuthViewModel, viewModel_BLE: TempPulseSpO2ViewModel = hiltViewModel()){
    val valuesList = remember { viewModel_BLE.getAllAlarms }.collectAsState(initial = listOf())
    val permissionState = rememberMultiplePermissionsState(permissions = PermissionUtils.permissions)
    val lifecycleOwner = LocalLifecycleOwner.current
    val bleConnectionState = viewModel_BLE.connectionState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color.Blue, Color.Green))),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

    ){
        DisposableEffect(key1 = lifecycleOwner,
            effect = {
                val observer = LifecycleEventObserver{_,event->
                    if(event == Lifecycle.Event.ON_START){
                        permissionState.launchMultiplePermissionRequest()
                        if(permissionState.allPermissionsGranted && bleConnectionState == ConnectionState.Disconnected){
                            viewModel_BLE.reconnect()
                        }
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)

                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }
        )

        LaunchedEffect(key1 = permissionState.allPermissionsGranted){
            if(permissionState.allPermissionsGranted){
                if(bleConnectionState == ConnectionState.Uninitialized) {
                    viewModel_BLE.initializeConnection()
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                .border(
                    BorderStroke(
                        5.dp, Color.Blue
                    ),
                    RoundedCornerShape(10.dp)
                )
                .height(250.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                if(bleConnectionState == ConnectionState.CurrentlyInitializing){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        CircularProgressIndicator()
                        if(viewModel_BLE.initializingMessage != null){
                            androidx.compose.material.Text(
                                text = viewModel_BLE.initializingMessage!!
                            )
                        }
                    }
                }else if(!permissionState.allPermissionsGranted){
                    androidx.compose.material.Text(
                        text = "Go to the app setting and allow the missing permissions.",
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(10.dp),
                        textAlign = TextAlign.Center
                    )
                }else if(viewModel_BLE.errorMessage != null){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        androidx.compose.material.Text(
                            text = viewModel_BLE.errorMessage!!
                        )
                        Button(
                            onClick = {
                                if(permissionState.allPermissionsGranted){
                                    viewModel_BLE.initializeConnection()
                                }
                            }
                        ) {
                            androidx.compose.material.Text(
                                "Try again"
                            )
                        }
                    }
                }else if(bleConnectionState == ConnectionState.Connected){
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ){
                        val values = parseVitals(viewModel_BLE.message)
                        val temp = values.first
                        val pulse = values.second
                        val spo2 = values.third
                        Text(
                            text = "Temperature: $temp",
                            style = MaterialTheme.typography.h6,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,

                        )
                        Text(
                            text = "Pulse: $pulse",
                            style = MaterialTheme.typography.h6,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,

                        )
                        Text(
                            text = "SpO2: $spo2",
                            style = MaterialTheme.typography.h6,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,

                        )
                        val currentTime = remember { mutableStateOf(LocalDateTime.now()) }
                        val formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss")
                        val formatterDate = DateTimeFormatter.ofPattern("yyyy.MM.dd")
                        LaunchedEffect(Unit) {
                            while (true) {
                                delay(10000L)
                                currentTime.value = LocalDateTime.now()
                            }
                        }
                        LaunchedEffect(currentTime.value) {
                            val date = currentTime.value.format(formatterDate).toString()
                            val time = currentTime.value.format(formatterTime).toString()
                            viewModel_BLE.addValues(
                                healthValues(
                                    dateTime = " ${time} - ${date}",
                                    temperature = values.first.toString(),
                                    BPM = values.second.toString(),
                                    SpO2 = values.third.toString()
                                )
                            )
                            val result = validation(temp,pulse,spo2)
                            for (i in 0..2)
                            {
                                if(result[i]==0 && i == 0)
                                {

                                    viewModel_BLE.addAlarms(
                                        alarms(
                                            dateTime = " ${time} - ${date}",
                                            alarm = "Unusual value: Temp = $temp *C"
                                        )
                                    )
                                }
                                if(result[i]==0 && i == 1)
                                {

                                    viewModel_BLE.addAlarms(
                                        alarms(
                                            dateTime = " ${time} - ${date}",
                                            alarm = "Unusual value: Pulse = $pulse BPM"
                                        )
                                    )
                                }
                                if(result[i]==0 && i == 2)
                                {

                                    viewModel_BLE.addAlarms(
                                        alarms(
                                            dateTime = " ${time} - ${date}",
                                            alarm = "Unusual value: SpO2 = $spo2 %"
                                        )
                                    )
                                }
                            }
                        }
                    }
                }else if(bleConnectionState == ConnectionState.Disconnected){
                    Button(onClick = {
                        viewModel_BLE.initializeConnection()
                    }) {
                        androidx.compose.material.Text("Initialize again")
                    }
                }
            }
        }
        Button(
            onClick = {
                viewModel_BLE.deleteAllAlarms()
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Delete Alarms", fontSize = 24.sp, color = Color.White)
        }
        SelectionContainer {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(valuesList.value.reversed()) { values ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        )
                        {
                            Row {
                                Text(text = "Warning!  ", fontWeight = FontWeight.ExtraBold, color = Color.Red)
                                Text(text = values.dateTime, fontWeight = FontWeight.ExtraBold)
                            }
                            Text(text = values.alarm, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
        }
    }
}