package eu.test.healthmonitorsmartwatch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.test.healthmonitorsmartwatch.ui.theme.AuthViewModel
import eu.test.healthmonitorsmartwatch.ui.theme.signUpViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreenHMSW(
    navigationToAppScreen:()->Unit,
    viewModel: signUpViewModel,
    authViewModel: AuthViewModel){
    val optionAccountType = listOf( "Pacient", "Doctor")
    var expendedAccountType = remember {
        mutableStateOf(false)
    }
    var firstNameCorrect = 0
    var lastNameCorrect = 0
    var emailCorrect = 0
    var passwordCorrect = 0
    val  showResult = remember {
        mutableStateOf(false)
    }
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color.Blue, Color.Green)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Type the required", fontSize = 24.sp, color = Color.White)
            OutlinedTextField(
                value = viewModel.firstName.value, onValueChange = {
                    viewModel.firstName.value = it
                },
                label = { Text(text = "First Name") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(color = Color.White,shape = RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = viewModel.lastName.value, onValueChange = {
                viewModel.lastName.value = it

            },
                label = { Text(text = "Last Name") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(color = Color.White,shape = RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = viewModel.Email.value, onValueChange = {
                viewModel.Email.value = it

            },
                label = { Text(text = "E-mail") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(color = Color.White,shape = RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = viewModel.Password.value, onValueChange = {
                    viewModel.Password.value = it
                    for (char in viewModel.Password.value) {
                        if (char.isDigit()) {
                            passwordCorrect = 1
                        }
                    }

                },
                label = { Text(text = "Password") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(color = Color.White,shape = RoundedCornerShape(12.dp)),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text("Account Type:", fontSize = 24.sp, color = Color.White)
            Box {
                //Input
                Button(
                    onClick = { expendedAccountType.value = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(viewModel.selectedAccountType.value, fontSize = 24.sp, color = Color.White)
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "Arrow down"
                    )
                }
                DropdownMenu(
                    expanded = expendedAccountType.value,
                    onDismissRequest = { expendedAccountType.value = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    DropdownMenuItem(
                        text = { Text(optionAccountType[0]) },
                        onClick = {
                            expendedAccountType.value = false
                            viewModel.selectedAccountType.value = optionAccountType[0]
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(optionAccountType[1]) },
                        onClick = {
                            expendedAccountType.value = false
                            viewModel.selectedAccountType.value = optionAccountType[1]
                        }
                    )
                }
            }
            Button(
                onClick = {
                    showResult.value = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Sign Up",fontSize = 24.sp)
            }
            if (showResult.value) {
                if (viewModel.firstName.value.firstOrNull()?.isUpperCase() == true) {
                    firstNameCorrect = 1
                }
                if (viewModel.lastName.value.firstOrNull()?.isUpperCase() == true) {
                    lastNameCorrect = 1
                }
                if (viewModel.Email.value.endsWith("@yahoo.com")) {
                    emailCorrect = 1
                }
                for (char in viewModel.Password.value) {
                    if (char.isDigit()) {
                        passwordCorrect = 1
                    }
                }
                if (firstNameCorrect == 1 && lastNameCorrect == 1 && emailCorrect == 1 && passwordCorrect == 1) {
                    val friends: List<String> = emptyList()
                    authViewModel.currentUserEmail.value = viewModel.Email.value.lowercase()
                    authViewModel.currentUserFirstName.value =viewModel.firstName.value
                    authViewModel.currentUserLastName.value = viewModel.lastName.value
                    authViewModel.currentUserAccountType.value = viewModel.selectedAccountType.value
                    authViewModel.signUp(viewModel.Email.value.lowercase(), viewModel.Password.value,viewModel.firstName.value, viewModel.lastName.value, viewModel.selectedAccountType.value, friends)
                    navigationToAppScreen()
                    showResult.value = false
                } else {
                    AlertDialog(onDismissRequest = { showResult.value = false }) {
                        Column(
                            modifier = Modifier
                                .background(Color.White)
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (firstNameCorrect == 0) {
                                Text(text = "Incorrect First Name format!")
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            if (lastNameCorrect == 0) {
                                Text(text = "Incorrect Last Name format!")
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            if (emailCorrect == 0) {
                                Text(text = "Incorrect E-mail format!")
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            if (passwordCorrect == 0) {
                                Text(text = "Incorrect Password format!")
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}


