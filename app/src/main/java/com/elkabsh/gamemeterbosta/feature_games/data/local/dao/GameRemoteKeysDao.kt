package com.elkabsh.gamemeterbosta.feature_games.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elkabsh.gamemeterbosta.feature_games.data.local.entity.GameRemoteKeys

@Dao
interface GameRemoteKeysDao {
    @Query("SELECT * FROM game_remote_keys WHERE gameId = :id AND category = :category")
    suspend fun getRemoteKeys(id: Int, category: String): GameRemoteKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKeys(keys: List<GameRemoteKeys>)

    @Query("DELETE FROM game_remote_keys") suspend fun clearRemoteKeys()

    @Query("DELETE FROM game_remote_keys WHERE category = :category")
    suspend fun clearRemoteKeysByCategory(category: String)

    @Query("SELECT MAX(lastUpdated) FROM game_remote_keys WHERE category = :category")
    suspend fun getLastUpdated(category: String): Long?
}
