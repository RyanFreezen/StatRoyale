package com.ryan.fortnite.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ryan.fortnite.FortniteBlue
import com.ryan.fortnite.FortniteBrightYellow
import com.ryan.fortnite.R
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun HomeScreen(navController: NavHostController) {
    var gamerTag by remember { mutableStateOf("") }
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userEmail = currentUser?.email ?: ""

    HomeScreenContent(navController, gamerTag, currentUser, userEmail) { tag ->
        gamerTag = tag
    }
}

@Composable
fun HomeScreenContent(
    navController: NavHostController,
    gamerTag: String,
    currentUser: FirebaseUser?,
    userEmail: String,
    onFieldChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FortniteBlue),
        contentAlignment = Alignment.TopCenter
    ) {
        UserAuthenticationArea(navController, currentUser)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(0.dp))
            FortniteLogo()
            Spacer(Modifier.height(0.dp))
            Text(
                "Stat Royale",
                style = MaterialTheme.typography.h4.copy(
                    fontWeight = Bold,
                    color = Color.White,
                    lineHeight = 60.sp
                )
            )
            Text(
                "Enter Your Fortnite Username",
                style = MaterialTheme.typography.h6.copy(
                    color = Color.White,
                    lineHeight = 8.sp
                )
            )
            Spacer(Modifier.height(16.dp))
            InputFields(gamerTag, onFieldChange)
            Spacer(Modifier.height(16.dp))
            ActionButton(navController, gamerTag)
            Spacer(Modifier.height(199.dp))
            Text(
                "Disclaimer: We are not affiliated, associated, authorized, endorsed by, " +
                        "or in any way officially connected with Epic Games.",
                style = MaterialTheme.typography.body2.copy(
                    color = FortniteBrightYellow,
                    textAlign = Center,
                    fontWeight = Bold,
                    fontSize = 12.sp
                )
            )
            Spacer(Modifier.height(1.dp))
        }
    }
}

/******************************************************************************************/

@Composable
fun FortniteLogo() {
    Image(
        painter = painterResource(id = R.drawable.fortnite_logo),
        contentDescription = "Fortnite Logo",
        modifier = Modifier
            .size(250.dp)
            .padding(0.dp)
    )
}

/******************************************************************************************/

@Composable
fun LogoutButton(onLogout: () -> Unit) {
    Button(
        onClick = onLogout,
        modifier = Modifier
    ) {
        Text(text = "Logout")
    }
}

@Composable
fun LoginSignupButton(onNavigate: () -> Unit) {
    Button(
        onClick = onNavigate,
        modifier = Modifier
    ) {
        Text(text = "Sign Up / Login")
    }
}

/******************************************************************************************/

@Composable
fun GamerTagField(gamerTag: String, onGamerTagChange: (String) -> Unit) {
    TextField(
        value = gamerTag,
        onValueChange = onGamerTagChange,
        label = { Text("FN Username", color = Color.White) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            cursorColor = Color.White,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f),
            backgroundColor = Color.Transparent,
        )
    )
}

@Composable
fun InputFields(gamerTag: String, onFieldChange: (String) -> Unit) {
    GamerTagField(gamerTag) { newGamerTag ->
        onFieldChange(newGamerTag)
    }
}

@Composable
fun ActionButton(navController: NavController, gamerTag: String) {
    Button(
        enabled = gamerTag.isNotBlank(),
        onClick = { navController.navigate("stats-screen/${URLEncoder.encode(gamerTag, StandardCharsets.UTF_8.toString())}") }
    ) {
        Text("Go")
    }
}

/******************************************************************************************/

@Composable
fun UserAuthenticationArea(navController: NavController, currentUser: FirebaseUser?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (currentUser != null) {
            Text(
                text = currentUser.email ?: "No Email",
                style = MaterialTheme.typography.body1.copy(color = Color.White),
                modifier = Modifier.weight(1f)
            )
            LogoutButton {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("signup-login")
            }
        } else {
            Spacer(Modifier.weight(1f))
            LoginSignupButton {
                navController.navigate("signup-login")
            }
        }
    }
}