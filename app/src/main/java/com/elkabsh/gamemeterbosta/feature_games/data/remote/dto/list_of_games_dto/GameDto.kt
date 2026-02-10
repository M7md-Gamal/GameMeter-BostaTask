package com.elkabsh.gamemeterbosta.feature_games.data.remote.dto.list_of_games_dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameDto(
    @SerialName("id")
    val id: Int,
    @SerialName("rating")
    val rating: Double,
    @SerialName("background_image")
    val backgroundImage: String,
    @SerialName("name")
    val name: String,
    @SerialName("released")
    val released: String,
    @SerialName("genres")
    val category: List<GenreDto>
)