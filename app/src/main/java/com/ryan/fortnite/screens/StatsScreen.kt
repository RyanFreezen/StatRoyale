package com.ryan.fortnite.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ryan.fortnite.FortniteBlue
import com.ryan.fortnite.FortniteDarkBlue
import com.ryan.fortnite.api.fetchStats
import com.ryan.fortnite.data.StatsData

@Composable
fun StatsScreen(gamerTag: String) {
    var stats by remember { mutableStateOf<StatsData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(gamerTag) {
        fetchStats(gamerTag, onSuccess = {
            stats = it
            isLoading = false
        }, onError = {
            errorMessage = it
            isLoading = false
        })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FortniteBlue),
            contentAlignment = Alignment.Center
    ){
        when {
            isLoading -> CircularProgressIndicator()
            errorMessage != null -> Text("Error: $errorMessage")
            stats != null -> DisplayStats(stats!!)
        }
    }
}

/******************************************************************************************/

@Composable
fun DisplayStats(stats: StatsData) {
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "${stats.account.name}'s Stats",
                style = MaterialTheme.typography.headlineMedium.copy(
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            StatSection("Performance", listOf(
                StatItemData("Score", stats.stats.all.overall.score.toString()),
                StatItemData("Score per Minute", stats.stats.all.overall.scorePerMin.toString()),
                StatItemData("Score per Match", stats.stats.all.overall.scorePerMatch.toString())
            ))
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            StatSection("Combat", listOf(
                StatItemData("Wins", stats.stats.all.overall.wins.toString()),
                StatItemData("Top 3", stats.stats.all.overall.top3?.toString() ?: "N/A"),
                StatItemData("Kills", stats.stats.all.overall.kills.toString()),
                StatItemData("Kills per Match", stats.stats.all.overall.killsPerMatch.toString()),
                StatItemData("Deaths", stats.stats.all.overall.deaths.toString()),
                StatItemData("Kill/Death Ratio", stats.stats.all.overall.kd.toString())
            ))
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            StatSection("Match Insights", listOf(
                StatItemData("Matches Played", stats.stats.all.overall.matches.toString()),
                StatItemData("Minutes Played", stats.stats.all.overall.minutesPlayed.toString()),
                StatItemData("Players Outlived", stats.stats.all.overall.playersOutlived.toString()),
                StatItemData("Win Rate", "${stats.stats.all.overall.winRate}%")
            ))
        }
    }
}

/******************************************************************************************/

@Composable
fun StatSection(title: String, items: List<StatItemData>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Left
            ),
            modifier = Modifier.fillMaxWidth()
        )
        items.forEach { item ->
            StatItem(label = item.label, value = item.value)
        }
    }
}

/******************************************************************************************/

data class StatItemData(val label: String, val value: String)

/******************************************************************************************/

@Composable
fun StatItem(label: String, value: String) {
    val formattedValue = formatNumber(value)
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .border(1.dp, Color.White, RoundedCornerShape(4.dp)),
        elevation = 2.dp,
        backgroundColor = FortniteDarkBlue,
        contentColor = Color.White
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(text = formattedValue, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        }
    }
}

/******************************************************************************************/

// Function to format numbers with up to one decimal place, only if needed
private fun formatNumber(numberStr: String): String {
    return try {
        val number = numberStr.toDouble()
        if (number % 1.0 == 0.0) {
            // If the number is effectively an integer, format without a decimal point
            number.toInt().toString()
        } else {
            // Otherwise, format with one decimal place
            String.format("%.1f", number)
        }
    } catch (e: NumberFormatException) {
        numberStr
    }
}