package com.ryan.fortnite.data

import com.google.firebase.firestore.PropertyName

data class User(
    @get:PropertyName("friendList")
    @set:PropertyName("friendList")
    var friendList: List<String> = emptyList(),
    var friendRequests: List<String> = emptyList()
)
