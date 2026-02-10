package com.elkabsh.gamemeterbosta.feature_games.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elkabsh.gamemeterbosta.feature_games.data.local.entity.GameDetailsEntity

@Dao
interface GameDetailsDao {
    @Query("SELECT * FROM game_details WHERE id = :gameId")
    suspend fun getGameDetails(gameId: Int): GameDetailsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameDetails(details: GameDetailsEntity)

    @Query("DELETE FROM game_details WHERE id = :gameId")
    suspend fun deleteGameDetails(gameId: Int)
}