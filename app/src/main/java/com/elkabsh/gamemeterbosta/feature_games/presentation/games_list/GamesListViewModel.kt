package com.elkabsh.gamemeterbosta.feature_games.presentation.games_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.elkabsh.gamemeterbosta.feature_games.domain.repo.GamesRepo
import com.elkabsh.gamemeterbosta.feature_games.presentation.games_list.GamesListUIEvent.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GamesListViewModel(
        private val gamesRepo: GamesRepo,
) : ViewModel() {
    private val _state = MutableStateFlow(GamesListState())
    val state = _state.asStateFlow()

    private val _uiEvents = Channel<GamesListUIEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    init {
        loadGames()
    }

    fun onAction(action: GamesListAction) {
        when (action) {
            is GamesListAction.UpdateSearchQuery -> {
                _state.update { it.copy(searchQuery = action.query) }
                loadGames()
            }
            is GamesListAction.SelectCategory -> {
                _state.update { it.copy(selectedCategory = action.category) }
                loadGames()
            }
            is GamesListAction.NavigateToDetails -> {
                viewModelScope.launch {
                    _uiEvents.send(NavigateToDetails(action.gameId))
                }
            }

            GamesListAction.Retry -> loadGames()
        }
    }

    private fun loadGames() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            _state.update {
                it.copy(
                        gamesFlow =
                                gamesRepo
                                        .getGamesByGenrePaged(
                                                _state.value.selectedCategory,
                                                _state.value.searchQuery
                                        )
                                        .cachedIn(viewModelScope)
                )
            }
        }
    }
}
