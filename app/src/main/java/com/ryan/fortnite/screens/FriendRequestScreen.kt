package com.ryan.fortnite.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.ryan.fortnite.FortniteBlue
import com.ryan.fortnite.FortniteBrightYellow
import com.ryan.fortnite.FortniteDarkBlue
import com.ryan.fortnite.auth
import com.ryan.fortnite.data.User

@SuppressLint("MutableCollectionMutableState")
@Composable
fun FriendRequestScreen(navController: NavController) {
    val currentUser = auth.currentUser

    if (currentUser != null) {
        val currentUserEmail = currentUser.email ?: ""
        var friendRequests by remember { mutableStateOf<List<String>?>(null) }
        val firestore = FirebaseFirestore.getInstance()

        /******************************************************************************************/

        fun handleFriendRequest(navController: NavController, email: String, isAccepted: Boolean) {
            val currentUserDocRef = firestore.collection("Users").document(currentUserEmail)
            val batch = firestore.batch()

            // Update friendRequests
            batch.update(currentUserDocRef, "friendRequests", FieldValue.arrayRemove(email))

            if (isAccepted) {
                // Update friendList
                batch.update(currentUserDocRef, "friendList", FieldValue.arrayUnion(email))

                // Get friend user's document reference
                val friendUserDocRef = firestore.collection("Users").document(email)

                // Update friend's friendList
                batch.update(friendUserDocRef, "friendList", FieldValue.arrayUnion(currentUserEmail))
            }

            batch.commit()
                .addOnSuccessListener {
                    // Transaction successful, handle post-transaction success
                    Log.d("FriendRequestScreen", "Friend request accepted successfully")
                    // Navigate back to FriendScreen
                    navController.popBackStack()
                }
                .addOnFailureListener { exception ->
                    // Transaction failed, handle error
                    Log.e("FriendScreen", "Error updating friend data: $exception")
                }
        }

        /******************************************************************************************/

        LaunchedEffect(Unit) {
            val currentUserDocRef = firestore.collection("Users").document(currentUserEmail)
            currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(User::class.java)
                    friendRequests = user?.friendRequests?.sorted()?.reversed()
                } else {
                    Log.e("FriendRequestScreen", "Current user document does not exist")
                }
            }.addOnFailureListener { exception ->
                Log.e("FriendRequestScreen", "Error retrieving current user document: $exception")
            }
        }

        /******************************************************************************************/

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
                    text = "Your Friend Requests",
                    style = MaterialTheme.typography.h4.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    friendRequests?.forEach { friendRequest ->
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 16.dp)
                            ) {
                                Text(
                                    text = friendRequest,
                                    style = MaterialTheme.typography.body1,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center,
                                    color = Color.White
                                )
                                Button(
                                    onClick = { handleFriendRequest(navController, friendRequest, true) },
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text("Accept")
                                }
                                Button(
                                    onClick = { handleFriendRequest(navController, friendRequest, false) },
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text("Decline")
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FortniteDarkBlue),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Please log in or create an account to view your friend requests!",
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