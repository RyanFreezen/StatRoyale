package com.ryan.fortnite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.ryan.fortnite.firebase.FireBase
import com.ryan.fortnite.firebase.addRandomChallenges
import com.ryan.fortnite.navigation.BottomNavigationBar
import com.ryan.fortnite.screens.AddFriendScreen
import com.ryan.fortnite.screens.ChallengeScreen
import com.ryan.fortnite.screens.FriendRequestScreen
import com.ryan.fortnite.screens.FriendScreen
import com.ryan.fortnite.screens.HomeScreen
import com.ryan.fortnite.screens.LoginSignUpScreen
import com.ryan.fortnite.screens.SignUpScreen
import com.ryan.fortnite.screens.StatsScreen
import com.ryan.fortnite.ui.theme.FortniteTheme


/******************************************************************************************/

val FortniteBlue = Color(0xFF0A74DA)
val FortniteYellow = Color(0xFFF3AF19)
val FortniteBrightYellow = Color(0xFFF2E218)
val FortniteDarkBlue = Color(0xFF053461)

val auth = FirebaseAuth.getInstance()

/******************************************************************************************/

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FortniteTheme {
                auth
                FireBase()
                addRandomChallenges()
                MainScreen()
            }
        }
    }
}

/******************************************************************************************/

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(navController, startDestination = "home") {
                composable("home") { HomeScreen(navController) }
                composable("challenges") { ChallengeScreen() }
                composable("friends") { FriendScreen(navController) }
                composable("signup-login") { LoginSignUpScreen(navController) }
                composable("signup") { SignUpScreen() }
                composable("addfriend") { AddFriendScreen() }
                composable("friend-requests") { FriendRequestScreen(navController) }
                composable("stats-screen/{gamerTag}") { backStackEntry ->
                    StatsScreen(gamerTag = backStackEntry.arguments?.getString("gamerTag") ?: "")
                }            }
        }
    }
}

/******************************************************************************************/