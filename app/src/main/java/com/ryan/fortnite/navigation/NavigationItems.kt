package com.ryan.fortnite.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(val screenLabel: String, val icon: ImageVector)
val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Filled.Home),
    BottomNavItem("Challenges", Icons.AutoMirrored.Filled.List),
    BottomNavItem("Friends", Icons.Filled.People),
)
