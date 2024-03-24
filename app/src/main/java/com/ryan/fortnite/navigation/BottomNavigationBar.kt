package com.ryan.fortnite.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ryan.fortnite.FortniteBrightYellow
// import com.ryan.fortnite.FortniteYellow

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    BottomNavigation(
        backgroundColor = FortniteBrightYellow,
        contentColor = Color.Black,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        bottomNavItems.forEach { navItem ->
            BottomNavigationItem(
                icon = { Icon(navItem.icon, contentDescription = null, tint=Color.Black) },
                label = { Text(navItem.screenLabel, fontWeight = FontWeight.Bold ) },
                selected = currentRoute == navItem.screenLabel,
                onClick = {
                    navController.navigate(navItem.screenLabel) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}