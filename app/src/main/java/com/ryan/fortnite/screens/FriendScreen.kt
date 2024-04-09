package com.ryan.fortnite.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.ryan.fortnite.FortniteBlue
import com.ryan.fortnite.FortniteBrightYellow
import com.ryan.fortnite.FortniteDarkBlue
import com.ryan.fortnite.auth
import com.ryan.fortnite.firebase.User

@Composable
fun FriendScreen(navController: NavController) {
    val currentUser = auth.currentUser

    if (currentUser != null) {
        val currentUserEmail = currentUser.email ?: ""

        // State for holding friend emails
        var friendEmails by remember { mutableStateOf<List<String>>(emptyList()) }

        // Firebase Firestore instance
        val firestore = FirebaseFirestore.getInstance()

        LaunchedEffect(Unit) {
            // Fetch the document corresponding to the current user's email
            val currentUserDocRef = firestore.collection("Users").document(currentUserEmail)
            currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Extract the friendList field from the document
                    val friendList = documentSnapshot.toObject(User::class.java)?.friendList
                    friendList?.let {
                        // Update the list of friend emails
                        friendEmails = it
                    }
                } else {
                    Log.e("FriendScreen", "Current user document does not exist")
                }
            }.addOnFailureListener { exception ->
                // Handle failure to retrieve user document
                Log.e("FriendScreen", "Error retrieving current user document: $exception")
            }
        }

        // Display friend emails
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Your Friends    ",
                        style = MaterialTheme.typography.h4.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        // Navigate to add friend screen
                        onClick = { navController.navigate("addfriend") },
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        Text(text = "Add Friend")
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    items(friendEmails) { friendEmail ->
                        // Display each friend email
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                            color = FortniteDarkBlue,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = friendEmail,
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier.padding(8.dp),
                                textAlign = TextAlign.Center,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    } else {
        // User is not logged in, display a message.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FortniteDarkBlue),
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