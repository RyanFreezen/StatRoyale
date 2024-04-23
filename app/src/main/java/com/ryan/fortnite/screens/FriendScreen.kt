package com.ryan.fortnite.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.ExtraBold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.ryan.fortnite.FortniteBlue
import com.ryan.fortnite.FortniteBrightYellow
import com.ryan.fortnite.FortniteDarkBlue
import com.ryan.fortnite.auth
import com.ryan.fortnite.data.User
import kotlinx.coroutines.tasks.await

@SuppressLint("MutableCollectionMutableState")
@Composable
fun FriendScreen(navController: NavController) {
    val currentUser = auth.currentUser

    if (currentUser != null) {
        val currentUserEmail = currentUser.email ?: ""
        var friendEmails by remember { mutableStateOf<List<String>?>(null) }
        var expandedDrop by remember { mutableStateOf(false) }
        var expanded by remember { mutableStateOf(false) }

        val firestore = FirebaseFirestore.getInstance()

        LaunchedEffect(Unit) {
            val currentUserDocRef = firestore.collection("Users").document(currentUserEmail)
            val documentSnapshot = currentUserDocRef.get().await()

            if (documentSnapshot.exists()) {
                val user = documentSnapshot.toObject(User::class.java)
                friendEmails = user?.friendList?.sorted()?.reversed()
            }
        }

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
                // Header Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Your Friends header
                    Text(
                        text = "Your Friends",
                        style = MaterialTheme.typography.h4.copy(
                            fontWeight = Bold,
                            color = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Hamburger menu
                    Box(
                        modifier = Modifier
                            .clickable { expandedDrop = true }
                            .align(Alignment.CenterVertically),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text("☰", color = FortniteBrightYellow, fontSize = 24.sp)
                    }
                }
                Box(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    DropdownMenu(
                        expanded = expandedDrop,
                        onDismissRequest = { expandedDrop = false },
                        modifier = Modifier.align(Alignment.TopEnd) .background(FortniteBrightYellow)
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                navController.navigate("addfriend")
                                expandedDrop = false
                            }
                        ) {
                            Text(text = "Add Friend", fontWeight = ExtraBold, fontSize = 20.sp)
                        }

                        DropdownMenuItem(
                            onClick = {
                                navController.navigate("friend-requests")
                                expandedDrop = false
                            }
                        ) {
                            Text(text = "Friend Requests", fontWeight = ExtraBold, fontSize = 20.sp)
                        }
                    }
                }
                // Friends list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    friendEmails?.take(if (expanded) friendEmails!!.size else 3)?.forEach { friendEmail ->
                        item {
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

                Spacer(modifier = Modifier.width(8.dp))

                // Show More/Show Less button
                if ((friendEmails?.size ?: 0) > 5) {
                    Button(
                        onClick = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (expanded) "Show Less" else "Show More",
                            style = MaterialTheme.typography.body1,
                            color = Color.White
                        )
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
                    fontWeight = Bold,
                    color = FortniteBrightYellow,
                    fontSize = 24.sp
                ),
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}