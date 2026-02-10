package com.elkabsh.gamemeterbosta.feature_games.domain.model

data class GameListItem(
    val id: Int,
    val name: String,
    val releaseDate: String,
    val backgroundImage: String,
    val rating: Double
)