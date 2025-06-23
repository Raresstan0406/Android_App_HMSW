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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import eu.test.healthmonitorsmartwatch.ui.theme.loginViewModel
import data.Result
import androidx.compose.runtime.livedata.observeAsState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenHMSW(navigationToAppScreen:()->Unit, viewModel1: loginViewModel, authViewModel: AuthViewModel) {
    val showResult = remember {
        mutableStateOf(false)
    }
    var passwordCorrect = 0
    var emailCorrect = 0
    val scrollState = rememberScrollState()
    val result by authViewModel.authResult.observeAsState()
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
            Spacer(modifier =  Modifier.height(16.dp))
            OutlinedTextField(
                value = viewModel1.Email.value, onValueChange = {
                    viewModel1.Email.value = it
                },
                label = { Text(text = "E-mail") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(12.dp))

            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = viewModel1.Password.value, onValueChange = {
                    viewModel1.Password.value = it
                    for (char in viewModel1.Password.value) {
                        if (char.isDigit()) {
                            passwordCorrect = 1
                        }
                    }
                },
                label = { Text(text = "Password")},
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(12.dp)),
                visualTransformation = PasswordVisualTransformation()

            )
            Spacer(modifier = Modifier.height(16.dp))
            if (viewModel1.Email.value.endsWith("@yahoo.com")) {
                emailCorrect = 1
            }
            for (char in viewModel1.Password.value) {
                if (char.isDigit()) {
                    passwordCorrect = 1
                }
            }
            Button(
                onClick = {
                    if (emailCorrect == 1 && passwordCorrect == 1) {
                        authViewModel.login(viewModel1.Email.value, viewModel1.Password.value)
                    } else {
                        showResult.value = true
                    }

                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Login", fontSize = 24.sp)
            }
            val user = authViewModel.userValues.value
            LaunchedEffect(result) {
                if (result is Result.Success) {
                    authViewModel.loadUserCurrent(viewModel1.Email.value)
                    // aici poți face navigare, sau alte acțiuni după ce datele sunt încărcate
                }
                if (result is Result.Error) {
                    showResult.value = true
                }
            }
            LaunchedEffect(user) {
                user?.let {
                    authViewModel.currentUserEmail.value = it.email ?: ""
                    authViewModel.currentUserFirstName.value = it.firstName ?: ""
                    authViewModel.currentUserLastName.value = it.lastName ?: ""
                    authViewModel.currentUserAccountType.value = it.accountType ?: ""
                    navigationToAppScreen()  // navighează când totul e gata
                }
            }

            if (showResult.value) {
                if (emailCorrect == 1 && passwordCorrect == 1) {
                    AlertDialog(onDismissRequest = { showResult.value = false }) {
                        Column(
                            modifier = Modifier
                                .background(Color.White)
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "No account with this data!")
                        }
                    }

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
