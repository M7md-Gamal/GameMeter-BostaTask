package com.elkabsh.gamemeterbosta.feature_games.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_details")
data class GameDetailsEntity(
    @PrimaryKey
    val id: Int,
    val description: String,
    val screenshotsUrl: String, // Store as JSON string
    val lastUpdated: Long = System.currentTimeMillis()
)