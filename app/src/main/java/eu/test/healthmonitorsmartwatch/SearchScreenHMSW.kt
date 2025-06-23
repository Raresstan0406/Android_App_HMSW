package eu.test.healthmonitorsmartwatch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import data.FriendItem
import data.UserItem
import eu.test.healthmonitorsmartwatch.ui.theme.AuthViewModel
import eu.test.healthmonitorsmartwatch.ui.theme.ChatroomViewModel

@Composable
fun SearchScreenHMSW( authViewModel: AuthViewModel,
    chatroomViewModel: ChatroomViewModel
){
    val users by chatroomViewModel.users.observeAsState(emptyList())
    val showDialog = remember { mutableStateOf(false) }
    val emails by authViewModel.friendEmails.observeAsState(emptyList())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color.Blue, Color.Green))),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Search people", fontSize = 24.sp, color = Color.White)
        Spacer(modifier = Modifier.height(10.dp))
        androidx.compose.material.Button(
            onClick = {
                showDialog.value = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(50)
        ) {
            Text(text = "Add", fontSize = 24.sp, color = Color.White)
        }
        if (showDialog.value){
            LaunchedEffect(authViewModel.currentUserAccountType.value){
                chatroomViewModel.loadSelectedUsers(authViewModel.currentUserAccountType.value, emails)
            }
            AlertDialog( onDismissRequest = { showDialog.value = true },
                title = { if (authViewModel.currentUserAccountType.value == "Pacient"){
                    Text(text = "Add Doctor", fontSize = 24.sp, color = Color.Black)
                }
                else if (authViewModel.currentUserAccountType.value == "Doctor"){
                    Text(text = "Add Pacient", fontSize = 24.sp, color = Color.Black)
                } },
                text={
                    LazyColumn {
                        items(users){user ->
                            UserItem(user = user, authViewModel)
                        }
                    }
                }, confirmButton = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        androidx.compose.material.Button(
                            onClick = { showDialog.value = false },
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Text(text = "Cancel", fontSize = 24.sp, color = Color.White)
                        }
                    }
                })
        }
        Text("Start a conversation:", fontSize = 24.sp, color = Color.White)
        Spacer(modifier = Modifier.height(12.dp))
        // Display a list of chat rooms
        LaunchedEffect(authViewModel.currentUserEmail) {
            authViewModel.getFriends()  // aici apelezi funcția care încarcă lista
        }
        LaunchedEffect(emails) {
            emails.forEach { email ->
                if (!authViewModel.userValuesMap.containsKey(email)) {
                    authViewModel.loadUserData(email)
                }
            }
        }
        LazyColumn {
            items(emails) { email ->
                FriendItem(email = email, chatroomViewModel, authViewModel)
            }
        }
    }
}


