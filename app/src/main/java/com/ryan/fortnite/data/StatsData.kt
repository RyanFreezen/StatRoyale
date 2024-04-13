package com.ryan.fortnite.data

import com.google.gson.annotations.SerializedName

// Response wrapper
data class StatsResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("data") val data: StatsData?
)

// Player account information
data class Account(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)

// Battle pass progress
data class BattlePass(
    @SerializedName("level") val level: Int,
    @SerializedName("progress") val progress: Int
)

// Root data class for player stats
data class StatsData(
    @SerializedName("account") val account: Account,
    @SerializedName("battlePass") val battlePass: BattlePass,
    @SerializedName("image") val image: String?,
    @SerializedName("stats") val stats: Stats
)

// Container for different game mode statistics
data class Stats(
    @SerializedName("all") val all: GameModeStats,
    @SerializedName("solo") val solo: GameModeStats,
    @SerializedName("duo") val duo: GameModeStats,
    @SerializedName("trio") val trio: GameModeStats?,
    @SerializedName("squad") val squad: GameModeStats,
    @SerializedName("ltm") val ltm: GameModeStats,
    @SerializedName("keyboardMouse") val keyboardMouse: GameModeStats,
    @SerializedName("gamepad") val gamepad: GameModeStats,
    @SerializedName("touch") val touch: GameModeStats?
)

// Detailed statistics for a specific game mode
data class GameModeStats(
    @SerializedName("overall") val overall: OverallStats
    // Potentially more fields per mode if needed
)

// Detailed statistics
data class OverallStats(
    @SerializedName("score") val score: Long,
    @SerializedName("scorePerMin") val scorePerMin: Double,
    @SerializedName("scorePerMatch") val scorePerMatch: Double,
    @SerializedName("wins") val wins: Int,
    @SerializedName("top3") val top3: Int?,
    @SerializedName("top5") val top5: Int?,
    @SerializedName("top6") val top6: Int?,
    @SerializedName("top10") val top10: Int,
    @SerializedName("top12") val top12: Int?,
    @SerializedName("top25") val top25: Int,
    @SerializedName("kills") val kills: Int,
    @SerializedName("killsPerMin") val killsPerMin: Double,
    @SerializedName("killsPerMatch") val killsPerMatch: Double,
    @SerializedName("deaths") val deaths: Int,
    @SerializedName("kd") val kd: Double,
    @SerializedName("matches") val matches: Int,
    @SerializedName("winRate") val winRate: Double,
    @SerializedName("minutesPlayed") val minutesPlayed: Int,
    @SerializedName("playersOutlived") val playersOutlived: Int,
    @SerializedName("lastModified") val lastModified: String
)
