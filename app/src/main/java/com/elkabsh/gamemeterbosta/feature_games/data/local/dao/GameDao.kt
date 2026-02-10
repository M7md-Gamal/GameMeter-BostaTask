package com.elkabsh.gamemeterbosta.feature_games.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elkabsh.gamemeterbosta.feature_games.data.local.entity.GameEntity

@Dao
interface GameDao {
    @Query("SELECT * FROM games INNER JOIN game_remote_keys ON games.id = game_remote_keys.gameId WHERE game_remote_keys.category LIKE '%' || :category || '%' AND games.name LIKE '%' || :query || '%' ORDER BY game_remote_keys.nextPage ASC, games.id ASC")
    fun getGamesByCategoryPaged(category: String, query: String): PagingSource<Int, GameEntity>

    @Query("SELECT * FROM games WHERE id = :gameId")
    suspend fun getGameById(gameId: Int): GameEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<GameEntity>)

    @Query("DELETE FROM games")
    suspend fun clearGames()

    @Query("DELETE FROM games WHERE id IN (SELECT gameId FROM game_remote_keys WHERE category LIKE '%' || :category || '%')")
    suspend fun clearGamesByCategory(category: String)
}
