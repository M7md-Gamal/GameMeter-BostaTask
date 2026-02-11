package com.elkabsh.gamemeterbosta.feature_games.presentation.game_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elkabsh.gamemeterbosta.core.domain.Result
import com.elkabsh.gamemeterbosta.core.presentation.toUiText
import com.elkabsh.gamemeterbosta.feature_games.domain.repo.GamesRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameDetailsViewModel(
    private val gamesRepo: GamesRepo,
    val gameId: Int,
) :
    ViewModel() {

    private val _state = MutableStateFlow(GameDetailsState())
    val state = _state.asStateFlow()

    private val _uiEvents = Channel<GameDetailsUIEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    init {
        loadGameDetails()
    }

    fun onAction(action: GameDetailsAction) {
        when (action) {
            GameDetailsAction.OnBackClicked -> {
                viewModelScope.launch {
                    _uiEvents.send(GameDetailsUIEvent.NavigateBack)
                }
            }

            GameDetailsAction.OnRetry -> loadGameDetails()
        }
    }

    private fun loadGameDetails() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = gamesRepo.getSingleGame(gameId)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            gameImg = result.data.backgroundImage,
                            gameName = result.data.name,
                            gameDescription = result.data.description,
                            gameReleaseDate = result.data.releaseDate,
                            gameRating = result.data.rating.toString(),
                            gameGenre = result.data.category,
                            gameScreenshots = result.data.screenshotsUrl,
                            error = null
                        )
                    }
                }

                is Result.Error -> {
                    _state.update {
                        it.copy(isLoading = false, error = result.error.toUiText())
                    }
                }
            }
        }
    }
}

