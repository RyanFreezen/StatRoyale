package com.ryan.fortnite.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AddFriendScreen(navController: NavController) {

    // State for search query and search results
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<String>>(emptyList()) }

    val firestore = FirebaseFirestore.getInstance()

    // Function to search for users
    fun searchUsers(query: String) {
        if (query.isNotBlank()) {
            // Perform search only if the query is not blank
            firestore.collection("Users")
                .document(query) // Search by document ID (email)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // User document found
                        // Store the user's email as a String
                        searchResults = listOf(query)
                    } else {
                        // User document not found
                        searchResults = emptyList()
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle search failure
                    Log.e("AddFriendScreen", "Error searching users: $exception")
                }
        } else {
            // If query is blank, clear search results
            searchResults = emptyList()
        }
    }

    // Trigger search when searchQuery changes
    LaunchedEffect(searchQuery) {
        searchUsers(searchQuery)
    }

    // Function to add friend
    fun addFriend(email: String) {
        // Get current user's email
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
        if (currentUserEmail != null) {
            val currentUserDocRef = firestore.collection("Users").document(currentUserEmail)

            firestore.runTransaction { transaction ->
                val currentUserDoc = transaction.get(currentUserDocRef)
                val friendList = currentUserDoc["friendList"] as? MutableList<String>

                if (friendList != null) {
                    // Check if the friend's email is not already in the friendList
                    if (!friendList.contains(email)) {
                        // Add the friend's email as the friend's document ID
                        friendList.add(email)
                        searchQuery = "Friend Added Successfully!"
                        // Update the friendList in the current user's document
                        transaction.update(currentUserDocRef, "friendList", friendList)

                        Log.d("AddFriendScreen", "Friend added successfully: $email")
                    } else {
                        Log.d("AddFriendScreen", "Friend already exists in the friend list")
                    }
                } else {
                    Log.e("AddFriendScreen", "FriendList is null")
                }
            }.addOnSuccessListener {
                // Transaction successful
            }.addOnFailureListener { exception ->
                // Transaction failed
                Log.e("AddFriendScreen", "Error adding friend: $exception")
            }
        } else {
            Log.e("AddFriendScreen", "Current user is null")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search for users") },
            modifier = Modifier.fillMaxWidth()
        )

        // Display search results
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(searchResults) { index, user ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(user) // Display user email here
                    Spacer(modifier = Modifier.weight(1f))
                    // Pass user email to addFriend function
                    Button(onClick = { addFriend(user) }) {
                        Text("Add Friend")
                    }
                }
            }
        }
    }
}
