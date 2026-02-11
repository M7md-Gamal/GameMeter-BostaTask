package com.elkabsh.gamemeterbosta.feature_games.data.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.elkabsh.gamemeterbosta.core.domain.EmptyResult
import com.elkabsh.gamemeterbosta.core.domain.Result
import com.elkabsh.gamemeterbosta.core.domain.errors.DataError
import com.elkabsh.gamemeterbosta.feature_games.data.local.GamesDB
import com.elkabsh.gamemeterbosta.feature_games.data.mappers.combineToGameDetail
import com.elkabsh.gamemeterbosta.feature_games.data.mappers.createGameDetailsEntityFromDTOs
import com.elkabsh.gamemeterbosta.feature_games.data.mappers.toGameListItem
import com.elkabsh.gamemeterbosta.feature_games.data.mappers.toGenre
import com.elkabsh.gamemeterbosta.feature_games.data.pagination.GameRemoteMediator
import com.elkabsh.gamemeterbosta.feature_games.data.remote.RemoteGameDataSource
import com.elkabsh.gamemeterbosta.feature_games.domain.model.GameDetailItem
import com.elkabsh.gamemeterbosta.feature_games.domain.model.GameListItem
import com.elkabsh.gamemeterbosta.feature_games.domain.model.GamesCategory
import com.elkabsh.gamemeterbosta.feature_games.domain.repo.GamesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GamesRepoImpl(
    private val remoteDataSource: RemoteGameDataSource, private val gameDatabase: GamesDB
) : GamesRepo {
    @OptIn(ExperimentalPagingApi::class)
    override fun getGamesByGenrePaged(
        category: GamesCategory, query: String
    ): Flow<PagingData<GameListItem>> {

        //for fetching only the local stored games on search
        val remoteMediator = if (query.isBlank()) {
            GameRemoteMediator(
                gameApi = remoteDataSource, gameDatabase = gameDatabase, category = category
            )
        } else {
            null
        }

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 5
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = {
                gameDatabase.gameDao()
                    .getGamesByCategoryPaged(
                        category.toGenre(),
                        query
                    )
            }
        ).flow.map { pagingData ->
            pagingData.map { gameEntity ->
                gameEntity.toGameListItem()
            }
        }
    }

    override suspend fun getSingleGame(id: Int): Result<GameDetailItem, DataError> =
        withContext(Dispatchers.IO) {
            try {
                var game = gameDatabase.gameDao().getGameById(id)
                var details = gameDatabase.gameDetailsDao().getGameDetails(id)

                if (details == null) {
                    val fetchResult = fetchGameDetails(id)
                    if (fetchResult is Result.Error) {
                        return@withContext Result.Error(fetchResult.error)
                    }
                    game = gameDatabase.gameDao().getGameById(id)
                    details = gameDatabase.gameDetailsDao().getGameDetails(id)
                }

                if (game != null && details != null) {
                    Result.Success(combineToGameDetail(game, details))
                } else {
                    Result.Error(DataError.Remote.UNKNOWN)
                }
            } catch (_: Exception) {
                Result.Error(DataError.Local.UNKNOWN)
            }
        }

    suspend fun fetchGameDetails(gameId: Int): EmptyResult<DataError> =
        withContext(Dispatchers.IO) {
            try {
                val descriptionDeferred = async { remoteDataSource.loadGameDescription(gameId) }
                val screenshotsDeferred = async { remoteDataSource.loadGameScreenshots(gameId) }

                val descriptionResult = descriptionDeferred.await()
                val screenshotsResult = screenshotsDeferred.await()

                when {
                    descriptionResult is Result.Success && screenshotsResult is Result.Success -> {
                        try {
                            gameDatabase.gameDetailsDao().insertGameDetails(
                                createGameDetailsEntityFromDTOs(
                                    gameId, screenshotsResult.data, descriptionResult.data
                                )
                            )
                            Result.Success(Unit)
                        } catch (_: Exception) {
                            Result.Error(DataError.Local.UNKNOWN)
                        }
                    }

                    descriptionResult is Result.Error -> descriptionResult
                    screenshotsResult is Result.Error -> screenshotsResult
                    else -> Result.Error(DataError.Remote.UNKNOWN)
                }
            } catch (_: Exception) {
                Result.Error(DataError.Unknown)
            }
        }
}
