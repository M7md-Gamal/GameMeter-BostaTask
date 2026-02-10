package com.elkabsh.gamemeterbosta.feature_games.data.mappers

import com.elkabsh.gamemeterbosta.feature_games.data.local.entity.GameDetailsEntity
import com.elkabsh.gamemeterbosta.feature_games.data.local.entity.GameEntity
import com.elkabsh.gamemeterbosta.feature_games.data.remote.dto.details_dto.DetailsResponseDto
import com.elkabsh.gamemeterbosta.feature_games.data.remote.dto.screenshots_dto.ScreenshotDto
import com.elkabsh.gamemeterbosta.feature_games.data.remote.dto.screenshots_dto.ScreenshotsResponseDto
import com.elkabsh.gamemeterbosta.feature_games.domain.model.GameDetailItem
import kotlinx.serialization.json.Json

fun createGameDetailsEntityFromDTOs(
        gameId: Int,
        screenshotsResponseDto: ScreenshotsResponseDto,
        detailsResponseDto: DetailsResponseDto
): GameDetailsEntity {
    return GameDetailsEntity(
            id = gameId,
            description = detailsResponseDto.description,
            screenshotsUrl = Json.encodeToString(screenshotsResponseDto.screenshotsDto)
    )
}

fun combineToGameDetail(game: GameEntity, details: GameDetailsEntity): GameDetailItem {
    val screenshotDtos: List<ScreenshotDto> = Json.decodeFromString(details.screenshotsUrl)
    val screenshots = screenshotDtos.map { it.image }

    return GameDetailItem(
            id = game.id,
            name = game.name,
            releaseDate = game.releaseDate,
            backgroundImage = game.backgroundImage,
            rating = game.rating,
            description = details.description,
            screenshotsUrl = screenshots,
            category = game.category
    )
}
