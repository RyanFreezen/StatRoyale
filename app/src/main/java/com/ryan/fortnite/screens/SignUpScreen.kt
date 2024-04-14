package com.ryan.fortnite.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import com.ryan.fortnite.FortniteBlue
import com.ryan.fortnite.FortniteYellow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen() {

    // Firebase Authentication instance
    val auth = FirebaseAuth.getInstance()

    // Firebase Firestore instance
    val firestore = FirebaseFirestore.getInstance()

    // State variables for user input and messages
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // CoroutineScope to launch coroutines
    val coroutineScope = rememberCoroutineScope()

    // Sign up function
    val signUp: () -> Unit = {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign up successful
                    val currentUser = auth.currentUser
                    // Store user email in Firestore Users collection with email as document ID
                    currentUser?.let { user ->
                        val userData = hashMapOf(
                            // Initialize friendList as empty
                            "friendList" to emptyList<String>()
                        )
                        firestore.collection("Users").document(user.email ?: "")
                            .set(userData)
                            .addOnSuccessListener {
                                // Sign up successful
                                successMessage = "Sign up successful! Welcome to StatRoyale!"
                                // Clear input fields
                                email = ""
                                password = ""
                                coroutineScope.launch {
                                    // Wait for 10 seconds
                                    delay(10000)
                                    // Clear success message
                                    successMessage = null
                                }
                            }
                            .addOnFailureListener { e ->
                                // Handle Firestore write failure
                                errorMessage = "Error storing user data: ${e.message}"
                                coroutineScope.launch {
                                    delay(10000)
                                    errorMessage = null
                                }
                            }
                    }
                } else {
                    // Sign up failed
                    val exception = task.exception
                    // Handle sign up failure
                    errorMessage = if (exception is FirebaseAuthUserCollisionException) {
                        // Email is already in use
                        "Email is already in use. Please use a different email."
                    } else {
                        // Other authentication errors
                        "Sign up failed. Please check your email and password."
                    }
                    // Launch coroutine to clear error message after 10 seconds
                    coroutineScope.launch {
                        delay(10000)
                        errorMessage = null
                    }
                }
            }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FortniteBlue),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Spacer to add vertical space
            Spacer(modifier = Modifier.height(16.dp))
            // Email input field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = Color.White) },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, FortniteYellow),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    backgroundColor = Color.Transparent
                )
            )
            // Spacer to add vertical space
            Spacer(modifier = Modifier.height(16.dp))
            // Password input field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = Color.White) },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, FortniteYellow),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    backgroundColor = Color.Transparent
                )
            )
            // Spacer to add vertical space
            Spacer(modifier = Modifier.height(16.dp))

            // Sign up button
            Button(
                onClick = signUp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Sign Up", color = Color.White)
            }
            // Success message
            successMessage?.let { message ->
                Text(
                    text = message,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            // Error message
            errorMessage?.let { message ->
                Text(
                    text = message,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}