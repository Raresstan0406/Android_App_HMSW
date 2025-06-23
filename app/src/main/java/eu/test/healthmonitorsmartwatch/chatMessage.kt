package eu.test.healthmonitorsmartwatch

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.bluetooth.BluetoothMessage
import java.util.Date
import java.util.Locale

@Composable
fun ChatMessage(
    message: BluetoothMessage,
){
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        horizontalArrangement =  Arrangement.SpaceEvenly
    ){
        val DandT = remember {
            SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        }
        var SpO2 = message.message.substringBefore(", ")
        val Pulse = message.message.substringAfter("Pulse:").substringBeforeLast(",")
        val Temperature = message.message.substringAfter("Temperature:")
        Text(text = "$DandT", fontSize = 20.sp, color = Color.Black)
        Text(text = "|",fontSize = 20.sp, color = Color.Black)
        if(SpO2.toFloatOrNull()?: 0 == 100 ||SpO2.toFloatOrNull()?: 0f <= 94){
            Text(text = SpO2, fontSize = 20.sp, color = Color.Red)
        }
        else{
            Text(text = SpO2, fontSize = 20.sp, color = Color.Black)
        }
        Text(text = "|",fontSize = 20.sp, color = Color.Black)
        if(Pulse.toFloatOrNull()?: 0f >= 100 || Pulse.toFloatOrNull()?: 0f <= 60){
            Text(text = Pulse, fontSize = 20.sp, color = Color.Red)
        }
        else{
            Text(text = Pulse, fontSize = 20.sp, color = Color.Black)
        }
        Text(text = "|",fontSize = 20.sp, color = Color.Black)
        if(Temperature.toFloatOrNull()?: 0f >= 37 || Temperature.toFloatOrNull()?: 0f <= 32){
            Text(text = Temperature, fontSize = 20.sp, color = Color.Red)
        }
        else{
            Text(text = Temperature, fontSize = 20.sp, color = Color.Black)
        }
    }
}