package com.ryan.fortnite.firebase

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FireBase {

    // Function to delete all challenges from the "challenges" collection
    /*
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


    private val db = Firebase.firestore

    // Reference to the "challenges" collection
    private val challengesCollection = db.collection("Challenges")

    // Function to add a challenge to the "challenges" collection
    /*
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

    // Function to get challenges from the "challenges" collection
    fun getChallenges(): Flow<List<Challenge>> = flow {
        val snapshot = challengesCollection.get().await()
        val challengesList = snapshot.toObjects(Challenge::class.java)
        emit(challengesList)
    }

}

// Function to generate random challenges and add them to the database
fun addRandomChallenges() {

    val firebase = FireBase() // Create an instance of the FireBase class

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
        // firebase.addChallenge(randomTitle, randomDifficulty) // Call addChallenge on the FireBase instance
    }
}

/******************************************************************************************/



/******************************************************************************************/

data class Challenge(
    val title: String = "",
    val difficulty: String = ""
)

data class User(
    val email: String = "",
)
