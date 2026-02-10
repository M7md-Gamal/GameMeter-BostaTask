package com.elkabsh.gamemeterbosta.feature_games.data.local.entity

import androidx.room.Entity

@Entity(tableName = "game_remote_keys", primaryKeys = ["gameId", "category"])
data class GameRemoteKeys(
        val gameId: Int,
        val category: String,
        val prevPage: Int?,
        val nextPage: Int?,
        val lastUpdated: Long = System.currentTimeMillis()
)
