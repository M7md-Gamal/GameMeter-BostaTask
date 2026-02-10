package com.elkabsh.gamemeterbosta.feature_games.presentation.games_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.elkabsh.gamemeterbosta.feature_games.domain.model.GamesCategory
import com.elkabsh.gamemeterbosta.feature_games.presentation.games_list.components.CategoryChips
import com.elkabsh.gamemeterbosta.feature_games.presentation.games_list.components.DiscoverHeader
import com.elkabsh.gamemeterbosta.feature_games.presentation.games_list.components.ErrorItem
import com.elkabsh.gamemeterbosta.feature_games.presentation.games_list.components.GameCard
import com.elkabsh.gamemeterbosta.feature_games.presentation.games_list.components.SearchBar
import com.elkabsh.gamemeterbosta.ui.theme.GameMeterBostaTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GamesListScreen(viewModel: GamesListViewModel = koinViewModel(), onNavigate: (Int) -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(true) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is GamesListUIEvent.NavigateToDetails -> onNavigate(event.gameId)
            }
        }
    }

    GamesListContent(state = state, onAction = viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesListContent(
        state: GamesListState,
        onAction: (GamesListAction) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
                color = MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
                shadowElevation = 2.dp
        ) {
            Column {
                DiscoverHeader()

                SearchBar(
                        query = state.searchQuery,
                        onQueryChange = { onAction(GamesListAction.UpdateSearchQuery(it)) },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )

                CategoryChips(
                        categories = GamesCategory.entries,
                        selectedCategory = state.selectedCategory,
                        onCategorySelected = { onAction(GamesListAction.SelectCategory(it)) }
                )
            }
        }

        val games = state.gamesFlow.collectAsLazyPagingItems()

        Box(modifier = Modifier.fillMaxSize()) {
            if (games.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (games.loadState.refresh is LoadState.Error) {
                val error = (games.loadState.refresh as LoadState.Error).error
                ErrorItem(
                        message = error.localizedMessage ?: "Unknown error occurred",
                        modifier = Modifier.fillMaxSize(),
                        onRetry = { games.retry() }
                )
            } else {
                LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                            count = games.itemCount,
                            key = games.itemKey { it.id },
                            contentType = games.itemContentType { "game" }
                    ) { index ->
                        val game = games[index]
                        if (game != null) {
                            GameCard(
                                    game = game,
                                    gamesCategory = state.selectedCategory,
                                    onDetailsClick = {
                                        onAction(GamesListAction.NavigateToDetails(game.id))
                                    }
                            )
                        }
                    }

                    when (val appendState = games.loadState.append) {
                        is LoadState.Loading -> {
                            item {
                                Box(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        contentAlignment = Alignment.Center
                                ) { CircularProgressIndicator() }
                            }
                        }
                        is LoadState.Error -> {
                            item {
                                ErrorItem(
                                        message = appendState.error.localizedMessage
                                                        ?: "Error loading more games",
                                        modifier = Modifier.fillMaxWidth(),
                                        onRetry = { games.retry() }
                                )
                            }
                        }
                        else -> {}
                    }
                }

                // Show empty state if list is truly empty and not loading
                if (games.itemCount == 0 && games.loadState.refresh is LoadState.NotLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No games found")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    GameMeterBostaTheme { GamesListContent(state = GamesListState(), onAction = {}) }
}
