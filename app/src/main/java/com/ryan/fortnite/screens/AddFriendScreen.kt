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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.ryan.fortnite.firebase.User

@Composable
fun AddFriendScreen(navController: NavController) {
    // State for search query and search results
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<User>>(emptyList()) }

    val firestore = FirebaseFirestore.getInstance()

    // Function to search for users
    fun searchUsers(query: String) {
        if (query.isNotBlank()) {
            // Perform search only if the query is not blank
            firestore.collection("Users")
                .whereEqualTo("email", query)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val results = querySnapshot.documents.mapNotNull { document ->
                        document.toObject(User::class.java)
                    }
                    searchResults = results
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

    // Function to add friend
    fun addFriend(friendUid: String) {
        // Get current user's UID
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.email
        if (currentUserUid != null) {
            val currentUserRef = firestore.collection("Users").document(currentUserUid)
            val friendListRef = currentUserRef.collection("friendList")
            val friendRef = friendListRef.document(friendUid)

            friendRef.set(mapOf("timestamp" to FieldValue.serverTimestamp()))
                .addOnSuccessListener {
                    // Friend added successfully
                    Log.d("AddFriendScreen", "Friend added successfully")
                }
                .addOnFailureListener { exception ->
                    // Handle friend addition failure
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

        // Button to trigger search
        Button(
            onClick = { searchUsers(searchQuery) },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Search")
        }

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
                    Text(user.email) // Display user information here
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { addFriend(user.email) }) {
                        Text("Add Friend")
                    }
                }
            }
        }
    }
}
