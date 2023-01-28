package com.adrianedilao.notesfirebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adrianedilao.notesfirebase.ui.MainViewModel
import com.adrianedilao.notesfirebase.ui.theme.NotesFirebaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesFirebaseTheme {
                // Set ViewModel
                val viewModel: MainViewModel = viewModel()
                // Set UI State
                val state by viewModel.state.collectAsState()

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // If user is logged in, open NotesScreenScreen() else, open LoginScreen()
                    if (state.isLoggedIn == true){
                        NotesScreen(viewModel = viewModel)
                    } else if (state.isLoggedIn == false){
                        LoginScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NotesFirebaseTheme {
        Greeting("Android")
    }
}