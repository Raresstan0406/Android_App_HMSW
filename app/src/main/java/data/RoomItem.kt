package data

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.test.healthmonitorsmartwatch.ui.theme.AuthViewModel

@Composable
fun RoomItem(room: Room, navigationToChatScreen:(String)->Unit, authViewModel: AuthViewModel) {
    val email1= room.name.substringAfter("Pacient: ").substringBefore(" -")
    val email2= room.name.substringAfter("Doctor: ")
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
            .height(80.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(authViewModel.currentUserEmail.value == email1)
            {
                val user = authViewModel.userValuesMap[email2]
                val firstNameFriend = user?.firstName ?: ""
                val lastNameFriend = user?.lastName ?: ""
                Text(text = "${firstNameFriend} ${lastNameFriend}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black)
            }
            else{
                val user = authViewModel.userValuesMap[email1]
                val firstNameFriend = user?.firstName ?: ""
                val lastNameFriend = user?.lastName ?: ""
                Text(text = "${firstNameFriend} ${lastNameFriend}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black)
            }
            Button(
                onClick = {
                    navigationToChatScreen(room.name)
                },
                shape = RoundedCornerShape(50),
            ) {
                Text(text = "Join", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}