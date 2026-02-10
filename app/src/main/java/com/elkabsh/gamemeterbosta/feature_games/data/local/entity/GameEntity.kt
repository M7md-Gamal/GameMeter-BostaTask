package com.elkabsh.gamemeterbosta.feature_games.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
        @PrimaryKey val id: Int,
        val name: String,
        val releaseDate: String,
        val backgroundImage: String,
        val rating: Double,
        val category: List<String>,
        val lastUpdated: Long = System.currentTimeMillis()
)
