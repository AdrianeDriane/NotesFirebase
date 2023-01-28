package com.adrianedilao.notesfirebase

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adrianedilao.notesfirebase.ui.MainViewModel
import com.adrianedilao.notesfirebase.ui.theme.NotesFirebaseTheme
import com.adrianedilao.notesfirebase.ui.theme.Yellow

@Composable
fun NotesScreen(viewModel: MainViewModel = viewModel()) {

    val state = viewModel.state.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.newNote()
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "")
            }
        }
    ) {
        Column {
            TopAppBar(
                title = { Text(color = Color.Black, text = "Notes") },
                backgroundColor = Yellow,
                elevation = 4.dp
            )

            NotesList(notes = state.value.notesList, viewModel = viewModel)
        }
    }
}

@Composable
fun NotesList(
    modifier: Modifier = Modifier,
    notes: List<Notes>,
    viewModel: MainViewModel
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(4.dp)
            ) {
                items(
                    items = notes,
                    itemContent = {
                        NoteCard(
                            note = it,
                            viewModel = viewModel
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun NoteCard(note: Notes, viewModel: MainViewModel) {

    var noteExpandedState by rememberSaveable {
        mutableStateOf(false)
    }

    val rotateState by animateFloatAsState(
        targetValue = if (noteExpandedState) 180f else 0f
    )

    var noteHeading by rememberSaveable {
        mutableStateOf(note.heading)
    }

    var noteContent by rememberSaveable {
        mutableStateOf(note.content)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 800,
                    easing = LinearOutSlowInEasing
                )
            )
            .padding(4.dp)
            .clickable { noteExpandedState = !noteExpandedState },
        border = BorderStroke(width = 1.dp, color = Color.Black)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .padding(horizontal = 8.dp)
                ) {
                    TextField(
                        value = noteHeading,
                        onValueChange = {
                            noteHeading = viewModel
                                .updateHeadingValue(text = it, id = note.id)
                        },
                        Modifier
                            .background(color = Color.Transparent)
                            .fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                        placeholder = {
                            Text(
                                text = "Heading",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        maxLines = 8,
                        enabled = noteExpandedState,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            noteHeading = "$noteHeading\n"
                        })
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { noteExpandedState = !noteExpandedState },
                        Modifier.rotate(rotateState)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "",
                            tint = Color.DarkGray
                        )
                    }
                    IconButton(onClick = { viewModel.removeItem(id = note.id) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "",
                            tint = Color.DarkGray
                        )
                    }
                }
            }
            AnimatedVisibility(visible = noteExpandedState) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    TextField(
                        value = noteContent,
                        onValueChange = {
                            noteContent = viewModel
                                .updateContentValue(text = it, id = note.id)
                        },
                        Modifier
                            .background(color = Color.Transparent)
                            .fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 16.sp),
                        placeholder = { Text("Content", fontSize = 16.sp) },
                        enabled = noteExpandedState,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            noteContent = "$noteContent\n"
                        })
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    NotesFirebaseTheme {
        //NotesScreen()
        var text by remember {
            mutableStateOf("")
        }
        TextField(
            value = text,
            onValueChange = { text = it },
            Modifier.background(color = Color.Transparent)
        )
    }
}