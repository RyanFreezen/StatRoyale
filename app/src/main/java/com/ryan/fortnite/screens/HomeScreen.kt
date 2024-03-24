package com.ryan.fortnite.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.ryan.fortnite.FortniteBlue

@Composable
fun HomeScreen(navController: NavHostController) {
    var gamerTag by remember { mutableStateOf("") }
    var platform by remember { mutableStateOf("") }

    // Retrieve current user's email
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userEmail = currentUser?.email ?: ""

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FortniteBlue), // Fortnite blue background
        contentAlignment = Alignment.CenterStart // Align content to top-left corner
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "StatRoyale",
                style = MaterialTheme.typography.h4.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Enter Your Fortnite Gamer Tag & Platform",
                style = MaterialTheme.typography.h6.copy(color = Color.White)
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Gamer Tag text field
            TextField(
                value = gamerTag,
                onValueChange = { gamerTag = it },
                label = { Text("Gamer Tag", color = Color.White) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f),
                    backgroundColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Platform text field
            TextField(
                value = platform,
                onValueChange = { platform = it },
                label = { Text("Platform (Xbox, Playstation, PC)", color = Color.White) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f),
                    backgroundColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Go button to fetch stats
            Button(
                onClick = { },
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(text = "Go")
            }
        }
    }

    // Align the button to the top end (top right) corner
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(3.dp),
        contentAlignment = Alignment.TopEnd
    ){
        // Conditional visibility of Logout button based on user authentication
        if (currentUser != null) {
            // Logout Button
            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    // Redirect to login/signup screen after logout
                    navController.navigate("signup/login")
                },
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(text = "Logout")
            }
        } else {
            // Login/Signup Button
            Button(
                // Navigate to signup screen
                onClick = { navController.navigate("signup/login") },
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(text = "Sign Up / Login")
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.TopStart
    ){
        // Display user's email if logged in
        if (currentUser != null) {
            Text(
                text = userEmail,
                style = MaterialTheme.typography.body1,
                color = Color.White
            )
        }
    }
}
