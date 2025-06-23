package data


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.test.healthmonitorsmartwatch.ui.theme.AuthViewModel
import eu.test.healthmonitorsmartwatch.ui.theme.ChatroomViewModel

@Composable
fun FriendItem(email:String, chatroomViewModel: ChatroomViewModel, authViewModel: AuthViewModel) {
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
            .height(90.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var added = remember { mutableStateOf(false) }
            val currentUser = authViewModel.currentUserEmail.value
            val currentAccountType = authViewModel.currentUserAccountType.value
            val user = authViewModel.userValuesMap[email]
            val firstNameFriend = user?.firstName ?: ""
            val lastNameFriend = user?.lastName ?: ""
            Text(
                text = "${firstNameFriend} ${lastNameFriend}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
            Button(
                onClick = {
                    if(currentAccountType == "Pacient"){
                        chatroomViewModel.createRoom("Pacient: $currentUser - Doctor: $email")
                    }
                    else{
                        chatroomViewModel.createRoom("Pacient: $email - Doctor: $currentUser")
                    }
                    added.value = true
                },
                shape = RoundedCornerShape(50)
            ) {
                if(added.value)
                {
                    Text(text = "✓", fontSize = 18.sp, color = Color.White)

                }
                else{
                    Text(text = "Create chat", fontSize = 18.sp, color = Color.White)
                }
            }
        }
    }
}