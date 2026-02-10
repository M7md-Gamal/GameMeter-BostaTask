package com.elkabsh.gamemeterbosta.feature_games.presentation.games_list

import androidx.paging.PagingData
import com.elkabsh.gamemeterbosta.core.presentation.UiText
import com.elkabsh.gamemeterbosta.feature_games.domain.model.GameListItem
import com.elkabsh.gamemeterbosta.feature_games.domain.model.GamesCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class GamesListState(
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val gamesFlow: Flow<PagingData<GameListItem>> = emptyFlow(),
    val searchQuery: String = "",
    val selectedCategory: GamesCategory = GamesCategory.ACTION
)
