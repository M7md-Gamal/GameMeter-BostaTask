package com.elkabsh.gamemeterbosta.feature_games.data.remote.dto.list_of_games_dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GamesListResponseDto(
    @SerialName("results")
    val results: List<GameDto>,
)