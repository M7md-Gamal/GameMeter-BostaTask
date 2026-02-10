package com.elkabsh.gamemeterbosta.feature_games.presentation.games_list

import com.elkabsh.gamemeterbosta.feature_games.domain.model.GamesCategory

sealed interface GamesListAction {
    data class UpdateSearchQuery(val query: String) : GamesListAction
    data class SelectCategory(val category: GamesCategory) : GamesListAction
    data class NavigateToDetails(val gameId: Int) : GamesListAction
}

sealed class GamesListUIEvent {
    data class NavigateToDetails(val gameId: Int) : GamesListUIEvent()
}
