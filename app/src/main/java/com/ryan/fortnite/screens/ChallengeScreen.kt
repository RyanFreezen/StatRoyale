package com.ryan.fortnite.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ryan.fortnite.firebase.FireBase
import com.ryan.fortnite.FortniteBlue
import com.ryan.fortnite.FortniteBrightYellow
import com.ryan.fortnite.FortniteDarkBlue
import com.ryan.fortnite.auth

@Composable
fun ChallengeScreen() {
    val currentUser = auth.currentUser

    // Check if the user is logged in
    if (currentUser != null) {
        // User is logged in, display the challenges

        // Create an instance of the FireBase class
        val firebase = FireBase()

        // Call the getChallenges function on the instance
        val challengesState = firebase.getChallenges().collectAsState(initial = emptyList())

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FortniteBlue), // Fortnite blue background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                item {
                    Text(
                        text = "Challenges",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = FortniteBrightYellow,
                            fontSize = 30.sp,
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(5f, 5f),
                            )
                        ),
                        modifier = Modifier.padding(bottom = 16.dp, top = 10.dp)
                    )
                }

                // Display fetched challenges with background and spacing
                items(challengesState.value) { challenge ->
                    Box(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                            .background(FortniteDarkBlue)
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = challenge.title,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Difficulty: ${challenge.difficulty}",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }
    } else {
        // User is not logged in, display a message or navigate to the login screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FortniteDarkBlue), // Displaying a red background as an example
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Please log in or create an account to view challenges!",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = FortniteBrightYellow,
                    fontSize = 24.sp
                ),
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
