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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AddFriendScreen() {

    // State for search query and search results
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<String>>(emptyList()) }

    val firestore = FirebaseFirestore.getInstance()

    // Function to search for users
    fun searchUsers(query: String) {
        if (query.isNotBlank()) {
            // Perform search only if the query is not blank
            firestore.collection("Users")
                // Search by document ID (email)
                .document(query)
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

    /******************************************************************************************/

    // Function to send friend request
    fun addFriend(email: String) {
        // Get current user's email
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
        if (currentUserEmail != null) {
            // Add friend request to the recipient's friendRequests collection
            val recipientDocRef = firestore.collection("Users").document(email)
            recipientDocRef.update("friendRequests", FieldValue.arrayUnion(currentUserEmail))
                .addOnSuccessListener {
                    searchQuery = ""
                    // Friend request sent successfully
                    Log.d("AddFriendScreen", "Friend request sent to $email")
                }
                .addOnFailureListener { exception ->
                    // Handle failure to send friend request
                    Log.e("AddFriendScreen", "Error sending friend request to $email: $exception")
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
            itemsIndexed(searchResults) { _, user ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    // Display user email here
                    Text(user)
                    Spacer(modifier = Modifier.weight(1f))
                    // Pass user email to addFriend function
                    Button(onClick = { addFriend(user) }) {
                        Text("Send Friend Request")
                    }
                }
            }
        }
    }
}