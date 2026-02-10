package com.elkabsh.gamemeterbosta.feature_games.data.pagination

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.elkabsh.gamemeterbosta.core.domain.Result
import com.elkabsh.gamemeterbosta.core.domain.errors.DataError
import com.elkabsh.gamemeterbosta.feature_games.data.local.GamesDB
import com.elkabsh.gamemeterbosta.feature_games.data.local.entity.GameEntity
import com.elkabsh.gamemeterbosta.feature_games.data.local.entity.GameRemoteKeys
import com.elkabsh.gamemeterbosta.feature_games.data.mappers.toGameEntity
import com.elkabsh.gamemeterbosta.feature_games.data.mappers.toGenre
import com.elkabsh.gamemeterbosta.feature_games.data.remote.RemoteGameDataSource
import com.elkabsh.gamemeterbosta.feature_games.domain.model.GamesCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class GameRemoteMediator(
    private val gameApi: RemoteGameDataSource,
    private val gameDatabase: GamesDB,
    private val category: GamesCategory? = null // Add genre filter parameter
) : RemoteMediator<Int, GameEntity>() {

    private val gameDao = gameDatabase.gameDao()
    private val remoteKeysDao = gameDatabase.gameRemoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GameEntity>
    ): MediatorResult {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(
                    "RemoteMediator",
                    "Load called - Type: $loadType, Category: ${category?.toGenre()}"
                )

                val page =
                    when (loadType) {
                        LoadType.REFRESH -> {
                            Log.d("RemoteMediator", "REFRESH - Starting from page 1")
                            1
                        }

                        LoadType.PREPEND -> {
                            Log.d("RemoteMediator", "PREPEND - Returning early")
                            return@withContext MediatorResult.Success(
                                endOfPaginationReached = true
                            )
                        }

                        LoadType.APPEND -> {
                            val remoteKeys = getRemoteKeyForLastItem(state)
                            val nextPage =
                                remoteKeys?.nextPage
                                    ?: return@withContext MediatorResult.Success(
                                        endOfPaginationReached = remoteKeys != null
                                    )
                            Log.d("RemoteMediator", "APPEND - Loading page $nextPage")
                            nextPage
                        }
                    }

                Log.d(
                    "RemoteMediator",
                    "Fetching page $page with pageSize ${state.config.pageSize}"
                )

                val result =
                    gameApi.loadListOfGames(
                        page = page,
                        pageSize = state.config.pageSize,
                        category = category?.toGenre()
                    )

                when (result) {
                    is Result.Success -> {
                        val response = result.data
                        val games = response.results
                        val endOfPaginationReached = games.isEmpty()

                        Log.d(
                            "RemoteMediator",
                            "Received ${games.size} games, endOfPagination: $endOfPaginationReached"
                        )

                        val safeCategory = category?.toGenre() ?: ""

                        gameDatabase.withTransaction {
                            if (loadType == LoadType.REFRESH) {
                                if (category != null) {
                                    Log.d("RemoteMediator", "Clearing category: $safeCategory")
                                    remoteKeysDao.clearRemoteKeysByCategory(safeCategory)
                                    gameDao.clearGamesByCategory(safeCategory)
                                } else {
                                    Log.d("RemoteMediator", "Clearing all games")
                                    remoteKeysDao.clearRemoteKeys()
                                    gameDao.clearGames()
                                }
                            }

                            val prevPage = if (page == 1) null else page - 1
                            val nextPage = if (endOfPaginationReached) null else page + 1

                            val keys =
                                games.map { game ->
                                    GameRemoteKeys(
                                        gameId = game.id,
                                        category = safeCategory,
                                        prevPage = prevPage,
                                        nextPage = nextPage
                                    )
                                }

                            Log.d(
                                "RemoteMediator",
                                "Inserting ${keys.size} remote keys and ${games.size} games"
                            )
                            remoteKeysDao.insertRemoteKeys(keys)
                            gameDao.insertGames(games.map { it.toGameEntity() })
                        }

                        MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                    }

                    is Result.Error -> {
                        Log.e("RemoteMediator", "Error loading games: ${result.error}")

                        // ✅ KEY FIX: Return success on network errors during REFRESH
                        // This allows PagingSource to show cached data
                        when {
                            loadType == LoadType.REFRESH && isNetworkError(result.error) -> {
                                Log.d(
                                    "RemoteMediator",
                                    "Network error during REFRESH - using cached data"
                                )
                                MediatorResult.Success(endOfPaginationReached = true)
                            }

                            loadType == LoadType.APPEND && isNetworkError(result.error) -> {
                                Log.d(
                                    "RemoteMediator",
                                    "Network error during APPEND - stopping pagination"
                                )
                                MediatorResult.Success(endOfPaginationReached = true)
                            }

                            else -> {
                                MediatorResult.Error(DataErrorException(result.error))
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("RemoteMediator", "Exception in load", e)

                // ✅ Also handle exceptions during REFRESH gracefully
                if (loadType == LoadType.REFRESH) {
                    Log.d("RemoteMediator", "Exception during REFRESH - using cached data")
                    MediatorResult.Success(endOfPaginationReached = true)
                } else {
                    MediatorResult.Error(e)
                }
            }
        }
    }

    // Helper function to identify network errors
    private fun isNetworkError(error: DataError): Boolean {
        return when (error) {
            is DataError.Remote -> true
            DataError.Unknown -> true // Treat UNKNOWN as potential network error
            else -> false
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, GameEntity>
    ): GameRemoteKeys? {
        val safeCategory = category?.toGenre() ?: ""
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { game ->
            remoteKeysDao.getRemoteKeys(game.id, safeCategory)
        }
    }
}

class DataErrorException(dataError: DataError) : Exception(dataError.toString())
