package com.adrianedilao.notesfirebase


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adrianedilao.notesfirebase.ui.MainViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adrianedilao.notesfirebase.ui.theme.NotesFirebaseTheme
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

@Composable
fun LoginScreen(viewModel: MainViewModel = viewModel()) { // Pass MainViewModel as viewModel

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(76, 78, 82))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                        .height(400.dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = 5.dp,
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.login),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = viewModel.email,
                            onValueChange = { viewModel.updateEmail(it) },
                            label = { Text(stringResource(R.string.email))},
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email
                            )
                        )

                        OutlinedTextField(
                            value = viewModel.password,
                            onValueChange = { viewModel.updatePassword(it) },
                            label = { Text(stringResource(R.string.password)) },
                            singleLine = true,
                            visualTransformation = if (viewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            trailingIcon = {
                                val image = if (viewModel.passwordVisible)
                                    Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff

                                // Provides localized description for accessibility services
                                val description = if (viewModel.passwordVisible) "Hide password" else "Show password"
                                
                                IconButton(onClick = { viewModel.passwordVisible = !viewModel.passwordVisible }) {
                                    Icon(imageVector = image, contentDescription = description)
                                }
                            }
                        )
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        Button(onClick = { viewModel.logIn(viewModel.email, viewModel.password) }) {
                            Text(text = stringResource(id = R.string.login).uppercase())
                        }

                        //TODO: Implement sign up feature
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    NotesFirebaseTheme {
        LoginScreen()
    }
}