package com.adrianedilao.notesfirebase.ui

import com.adrianedilao.notesfirebase.Notes
import java.lang.Exception

data class MainState(
    val isLoggedIn: Boolean?,
    val error: Exception?,
    val notesList: List<Notes>
)