package com.ryan.fortnite.screens

import android.widget.Toast
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.ryan.fortnite.FortniteBlue
import com.ryan.fortnite.FortniteYellow
import androidx.compose.ui.platform.LocalContext

@Composable
fun LoginSignUpScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userEmail = currentUser?.email ?: ""

    /******************************************************************************************/

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
            Spacer(modifier = Modifier.height(250.dp))
            // Email input field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = Color.White) },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, FortniteYellow),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    textColor = Color.White,
                    cursorColor = Color.White,
                    backgroundColor = Color.Transparent
                )
            )
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
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    textColor = Color.White,
                    cursorColor = Color.White,
                    backgroundColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Login button
            Button(
                onClick = {
                    // Login logic
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Login successful, navigate to the desired destination
                                navController.navigate("home")
                                Toast.makeText(
                                    context,
                                    "Welcome back! It's nice to see you 😁",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                // Login failed, handle the error (e.g., show a toast)
                                Toast.makeText(
                                    context,
                                    "Login failed. Please check your credentials and try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            ) {
                Text(text = "Login", color = Color.White)
            }
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                modifier = Modifier.padding(vertical = 16.dp),
                text = "Sign up if you don't have an account!",
                style = MaterialTheme.typography.h6.copy(color = Color.White)
            )
            // Sign up button
            Button(
                onClick = { navController.navigate("signup") },
            ) {
                Text(text = "Sign up", color = Color.White)
            }
            Spacer(modifier = Modifier.height(0.dp))
        }
    }
}
