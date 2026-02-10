package com.elkabsh.gamemeterbosta.feature_games.data.remote.dto.screenshots_dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScreenshotDto(
    @SerialName("image")
    val image: String
)