package eu.test.healthmonitorsmartwatch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import data.RoomItem
import eu.test.healthmonitorsmartwatch.ui.theme.AuthViewModel
import eu.test.healthmonitorsmartwatch.ui.theme.ChatroomViewModel

@Composable
fun ChatroomsScreenHMSW(navigationToChatScreen:(String)->Unit, authViewModel: AuthViewModel, chatroomViewModel: ChatroomViewModel){
    val rooms by chatroomViewModel.rooms.observeAsState(initial = emptyList())
    Column (modifier = Modifier
        .fillMaxSize()
        .background(Brush.verticalGradient(colors = listOf(Color.Blue, Color.Green))),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ){
        Text(text = "Chatrooms", fontSize = 24.sp, color = Color.White)
        chatroomViewModel.loadRooms(authViewModel.currentUserEmail.value)
        LazyColumn {
            items(rooms) { room ->
                RoomItem(room, navigationToChatScreen, authViewModel)
            }
        }
    }
}