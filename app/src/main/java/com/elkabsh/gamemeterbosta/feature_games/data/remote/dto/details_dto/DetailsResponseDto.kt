package com.elkabsh.gamemeterbosta.feature_games.data.remote.dto.details_dto

import com.elkabsh.gamemeterbosta.feature_games.data.remote.dto.list_of_games_dto.GenreDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailsResponseDto(
        @SerialName("id") val id: Int,
        @SerialName("description") val description: String,
        @SerialName("name") val name: String,
        @SerialName("released") val released: String? = "",
        @SerialName("background_image") val backgroundImage: String? = "",
        @SerialName("rating") val rating: Double? = 0.0,
        @SerialName("genres") val genres: List<GenreDto> = emptyList()
)
