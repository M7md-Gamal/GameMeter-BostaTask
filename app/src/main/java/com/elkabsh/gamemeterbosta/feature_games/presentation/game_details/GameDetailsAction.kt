package com.elkabsh.gamemeterbosta.feature_games.presentation.game_details

sealed interface GameDetailsAction {
    object OnBackClicked : GameDetailsAction
    object OnRetry : GameDetailsAction

}

sealed class GameDetailsUIEvent {
    object NavigateBack : GameDetailsUIEvent()
}