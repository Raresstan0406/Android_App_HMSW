package eu.test.healthmonitorsmartwatch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import domain.bluetooth.BluetoothDevice
import eu.test.healthmonitorsmartwatch.ui.theme.TempPulseSpO2ViewModel

@Composable
fun HealthValuesScreenHMSW(
    state: BluetoothUiState,
    onStartScan: () -> Unit,
    deviceConnect: (BluetoothDevice) -> Unit,
    viewModel_BLE: TempPulseSpO2ViewModel = hiltViewModel()
    //onDisconnect: () -> Unit,
    //onSendMessage: (String) -> Unit

){
    val valuesList = remember { viewModel_BLE.getAllValues }.collectAsState(initial = listOf())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color.Blue, Color.Green)))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement =  Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Time",fontSize = 20.sp, color = Color.White)
            Text(text = "|",fontSize = 20.sp, color = Color.White)
            Text(text = "Temperature",fontSize = 20.sp, color = Color.White)
            Text(text = "|",fontSize = 20.sp, color = Color.White)
            Text(text = "Pulse",fontSize = 20.sp, color = Color.White)
            Text(text = "|",fontSize = 20.sp, color = Color.White)
            Text(text = "SpO2",fontSize = 20.sp, color = Color.White)

        }
        androidx.compose.material.Button(
            onClick = {
                viewModel_BLE.deleteAllValues()
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Delete Health Values", fontSize = 24.sp, color = Color.White)
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        )
                        {
                            val temp = values.temperature
                            val pulse = values.BPM
                            val spo2 = values.SpO2
                            val date = values.dateTime.substringBefore(" -")
                            val time = values.dateTime.substringAfter("- ")
                            Column (
                                verticalArrangement = Arrangement.Center
                            ){
                                Text(text = time, fontWeight = FontWeight.ExtraBold)
                                Text(text = date, fontWeight = FontWeight.ExtraBold)
                            }
                            Text(text = ":",fontSize = 20.sp)
                            Text(text = "$temp *C", fontWeight = FontWeight.ExtraBold)
                            Text(text = "|",fontSize = 20.sp)
                            Text(text = "$pulse BPM", fontWeight = FontWeight.ExtraBold)
                            Text(text = "|",fontSize = 20.sp)
                            Text(text = "$spo2 %", fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
        }
        /*LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Button(onClick ={
                        onStartScan()
                        for (device in state.scannedDevices){
                            if(device.name == "ESP32_DEMO_HMSW"){
                                deviceConnect(device)
                            }
                        } },
                        modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)) {
                    Text(text = "Connect")
                }
            }
            items(state.messages) { message ->
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    ChatMessage(
                        message = message,
                        modifier = Modifier,
                    )
                }
            }
        }*/
    }
}

