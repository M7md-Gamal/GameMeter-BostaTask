package com.elkabsh.gamemeterbosta.feature_games.presentation.game_details

import com.elkabsh.gamemeterbosta.core.presentation.UiText

data class GameDetailsState(
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val gameImg: String = "",
    val gameName: String = "",
    val gameDescription: String ="",
    val gameReleaseDate: String = "",
    val gameRating: String = "",
    val gameGenre: List<String> = emptyList(),
    val gameScreenshots: List<String> = emptyList()
){}
