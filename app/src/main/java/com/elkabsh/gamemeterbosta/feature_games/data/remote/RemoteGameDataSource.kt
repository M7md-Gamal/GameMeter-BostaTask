package com.elkabsh.gamemeterbosta.feature_games.data.remote

import com.elkabsh.gamemeterbosta.core.domain.errors.DataError
import com.elkabsh.gamemeterbosta.core.domain.Result
import com.elkabsh.gamemeterbosta.feature_games.data.remote.dto.details_dto.DetailsResponseDto
import com.elkabsh.gamemeterbosta.feature_games.data.remote.dto.list_of_games_dto.GamesListResponseDto
import com.elkabsh.gamemeterbosta.feature_games.data.remote.dto.screenshots_dto.ScreenshotsResponseDto

interface RemoteGameDataSource {
    suspend fun loadListOfGames(
        page: Int,
        pageSize: Int,
        category: String?
    ): Result<GamesListResponseDto, DataError.Remote>
    suspend fun loadGameDescription(id:Int): Result<DetailsResponseDto, DataError.Remote>
    suspend fun loadGameScreenshots(id:Int): Result<ScreenshotsResponseDto, DataError.Remote>

}