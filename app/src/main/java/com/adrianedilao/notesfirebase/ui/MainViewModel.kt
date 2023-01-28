package com.adrianedilao.notesfirebase.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.adrianedilao.notesfirebase.Notes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    //Declare an instance of Firebase Auth and initialize Firebase Auth
    private var auth: FirebaseAuth = Firebase.auth

    //Set ViewModel Flow state
    //_state is accessible only inside of GameViewModel class and will not be affected by
    //changes outside of the class.
    private val _state = MutableStateFlow(
        MainState(
            isLoggedIn = null,
            error = null,
            notesList = emptyList()
        )
    )

    val db = Firebase.firestore

    //state is an immutable state flow version of the state which can be used to pass states
    //Read only and can only be changed by state which it is assigned to.
    val state = _state.asStateFlow()

    // Email input
    var email by mutableStateOf("")

    // Password input
    var password by mutableStateOf("")

    // Check if password is visible
    var passwordVisible by mutableStateOf(false)

    init {
        checkAuth()
    }

    //Check if user is logged in
    fun checkAuth() {
        if (auth.currentUser == null) {
            _state.value = _state.value.copy(isLoggedIn = false)
        } else {
            _state.value = _state.value.copy(isLoggedIn = true)
            observeNotes()
        }
    }

    //Update user input inside the email & Password TextBox in GameScreen.kt
    fun updateEmail(updatedEmail: String) {
        email = updatedEmail
    }

    fun updatePassword(updatedPassword: String) {
        password = updatedPassword
    }

    fun logIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    _state.value = _state.value.copy(isLoggedIn = true)
                    observeNotes()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    // If exception means invalid user, then sign up user, else change state error
                    if (task.exception is FirebaseAuthInvalidUserException) {
                        signUp(email, password)
                    } else {
                        _state.value = _state.value.copy(error = task.exception)
                    }
                }
            }
    }

    private fun signUp(email: String, password: String) {
        // Add user with arguments provided
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    _state.value = _state.value.copy(isLoggedIn = true)
                    observeNotes()
                } else {
                    //If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    _state.value = _state.value.copy(error = task.exception)
                }
            }
    }

    fun newNote() {
        val note = hashMapOf(
            "heading" to "",
            "content" to "",
            "created_at" to System.currentTimeMillis()
        )

        db.collection("notes")
            .add(note)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "New note added with ID: ${documentReference.id}")
            }
            .addOnFailureListener {
                Log.d(TAG, "Error adding document", it)
            }
    }

    //TODO: Fix bug
    fun removeItem(id: String) {
        /*val docRef = db.collection("notes").document(id)
        val updates = hashMapOf<String, Any>(
            "heading" to FieldValue.delete(),
            "content" to FieldValue.delete(),
            "created_at" to FieldValue.delete()
        )
        docRef.update(updates)
        docRef.delete()*/

        /*val listOfNotes = mutableListOf<Notes>()
        val collectionRef = db.collection("notes")

        collectionRef.addSnapshotListener{snapshot, exception ->
            if (exception != null){
                return@addSnapshotListener
            }

            snapshot?.let {
                listOfNotes.clear()
                for (doc in it){
                    val item = doc.toObject(Notes::class.java)
                    listOfNotes.add(item)
                }
            }
        }*/
    }

    private fun observeNotes() {
        db.collection("notes")
            .orderBy("created_at")
            .addSnapshotListener { value, _ ->
                value?.let {
                    val noteListFromFireStore = mutableListOf<Notes>()

                    for (doc in value) {
                        noteListFromFireStore.add(
                            Notes(
                                heading = doc.getString("heading")!!,
                                content = doc.getString("content")!!,
                                id = doc.id
                            )
                        )
                    }

                    _state.value = _state.value.copy(
                        notesList = noteListFromFireStore
                    )
                }
            }
    }

    fun updateHeadingValue(text: String, id: String): String {
        val docRef = db.collection("notes").document(id)
        docRef.update("heading", text)
        return text
    }

    fun updateContentValue(text: String, id: String): String {
        val docRef = db.collection("notes").document(id)
        docRef.update("content", text)
        return text
    }
}