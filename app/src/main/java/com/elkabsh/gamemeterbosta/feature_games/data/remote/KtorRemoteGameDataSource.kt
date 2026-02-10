package com.elkabsh.gamemeterbosta.feature_games.data.remote

import com.elkabsh.gamemeterbosta.core.data.remote.safeCall
import com.elkabsh.gamemeterbosta.core.domain.Result
import com.elkabsh.gamemeterbosta.core.domain.errors.DataError
import com.elkabsh.gamemeterbosta.feature_games.data.remote.dto.details_dto.DetailsResponseDto
import com.elkabsh.gamemeterbosta.feature_games.data.remote.dto.list_of_games_dto.GamesListResponseDto
import com.elkabsh.gamemeterbosta.feature_games.data.remote.dto.screenshots_dto.ScreenshotsResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class KtorRemoteGameDataSource(
    private val client: HttpClient
): RemoteGameDataSource {

    override suspend fun loadListOfGames(
        page: Int,
        pageSize: Int,
        category: String?
    ): Result<GamesListResponseDto, DataError.Remote> {
        return safeCall<GamesListResponseDto> {
            client.get("games"){
                parameter("page", page)
                parameter("page_size", pageSize)
                parameter("genres", category)
            }
        }
    }

    override suspend fun loadGameDescription(id: Int): Result<DetailsResponseDto, DataError.Remote> {
        return safeCall<DetailsResponseDto> {
            client.get("games/$id")
        }
    }

        override suspend fun loadGameScreenshots(id: Int): Result<ScreenshotsResponseDto, DataError.Remote> {
            return safeCall<ScreenshotsResponseDto> {
                client.get("games/$id/screenshots")
        }
    }
}