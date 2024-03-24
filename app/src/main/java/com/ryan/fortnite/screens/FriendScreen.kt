package com.ryan.fortnite.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.ryan.fortnite.FortniteBlue
import com.ryan.fortnite.FortniteBrightYellow
import com.ryan.fortnite.FortniteDarkBlue
import com.ryan.fortnite.auth

@Composable
fun FriendScreen(navController: NavController, currentUserUid: String) {
    val currentUser = auth.currentUser

    if (currentUser != null) {
        // State for holding friend list
        var friendIds by remember { mutableStateOf<List<String>>(emptyList()) }

        // Firebase Firestore instance
        val firestore = FirebaseFirestore.getInstance()

        // Retrieve friend list from Firestore
        LaunchedEffect(Unit) {
            val friendCollection = firestore.collection("Users").document(currentUserUid)
                .collection("friendList")
            friendCollection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                snapshot?.let {
                    friendIds = it.documents.map { document ->
                        document.id
                    }
                }
            }
        }

        // Display friend list
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FortniteBlue),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Your Friends",
                    style = MaterialTheme.typography.h4.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Button(
                    // Navigate to add friend screen
                    onClick = { navController.navigate("addfriend") },
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Text(text = "Add Friend")
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    items(friendIds) { friendId ->
                        // You can fetch user details based on friendId from Firestore here
                        // For example:
                        Text(
                            text = friendId,
                            style = MaterialTheme.typography.body1.copy(color = Color.White),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
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
                text = "Please log in or create an account to view your friends!",
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


