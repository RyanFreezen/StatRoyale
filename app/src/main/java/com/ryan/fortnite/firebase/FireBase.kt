package com.ryan.fortnite.firebase

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FireBase {

    /*
    // Un-comment when you want to delete Challenges !!!

    // Function to delete all challenges from the "challenges" collection
    fun deleteAllChallenges(challengesCollection: CollectionReference) {
        challengesCollection
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    document.reference.delete()
                }
                Log.d(TAG, "All challenges deleted successfully")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error deleting challenges", exception)
            }
    }

    */

    /******************************************************************************************/

    private val db = Firebase.firestore

    // Reference to the "Challenges" collection database in Firebase Firestore.
    private val challengesCollection = db.collection("Challenges")

    /******************************************************************************************/

    /*
     // Un-comment when you want to add Challenges !!!

    // Function to add a challenge to the "challenges" collection
    fun addChallenge(title: String, difficulty: String) {
        val newChallenge = hashMapOf(
            "title" to title,
            "difficulty" to difficulty
        )

        challengesCollection
            .add(newChallenge)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Challenge added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding challenge", e)
            }
    }

    */

    /******************************************************************************************/

    // Function to get challenges from the "challenges" collection
    fun getChallenges(): Flow<List<Challenge>> = flow {
        val snapshot = challengesCollection.get().await()
        val challengesList = snapshot.toObjects(Challenge::class.java)
        emit(challengesList)
    }

}

// Function to generate random challenges and add them to the database
fun addRandomChallenges() {

    // Create an instance of the FireBase class. (Un-comment when you want to delete Challenges)
    // val firebase = FireBase()
    // firebase.deleteAllChallenges(firebase.challengesCollection)

    val titles = listOf(
        "Build a sky base and win without touching the ground",
        "Win using only a common pistol and no building",
        "Land at every named location in a single match",
        "Win using only items found in chests",
        "Travel from one side of the map to the other using only a shopping cart",
        "Win without firing a single shot",
        "Get three consecutive kills using only a pickaxe",
        "Win without taking any damage",
        "Get a victory royale with zero eliminations",
        "Win using only items obtained from supply drops",
        "Build a maze and lead an opponent through it to win",
        "Win using only sniper rifles",
        "Win while only moving backwards",
        "Win with only 1 health point remaining",
        "Win without using any healing items",
        "Win with your screen inverted",
        "Win without jumping",
        "Win while only using traps to eliminate opponents",
        "Win with your controls randomized",
        "Win while spectating the entire time"
    )

    val difficulties = listOf("Easy", "Medium", "Hard")

    repeat(10) {
        val randomTitle = titles.random()
        val randomDifficulty = difficulties.random()

        /*
        * Call addChallenge on the FireBase instance
        * Only un-comment to add challenges once all challenges are first deleted !!!
        */
        // firebase.addChallenge(randomTitle, randomDifficulty)
    }
}

/******************************************************************************************/

data class Challenge(
    val title: String = "",
    val difficulty: String = ""
)

data class User(
    @get:PropertyName("friendList")
    @set:PropertyName("friendList")
    var friendList: List<String> = emptyList(),
    var friendRequests: List<String> = emptyList()
)

/******************************************************************************************/