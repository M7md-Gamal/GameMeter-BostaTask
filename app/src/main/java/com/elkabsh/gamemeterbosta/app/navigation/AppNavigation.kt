package com.elkabsh.gamemeterbosta.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.elkabsh.gamemeterbosta.feature_games.presentation.game_details.GameDetailsScreen
import com.elkabsh.gamemeterbosta.feature_games.presentation.game_details.GameDetailsViewModel
import com.elkabsh.gamemeterbosta.feature_games.presentation.games_list.GamesListScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Route.GamesList,
        modifier = modifier
    ) {
        composable<Route.GamesList> {
            GamesListScreen { id -> navController.navigate(Route.GameDetail(id)) }
        }
        composable<Route.GameDetail> {
            val id = it.toRoute<Route.GameDetail>().id
            GameDetailsScreen(
                viewModel = koinViewModel<GameDetailsViewModel>(parameters = { parametersOf(id) })
            ) { navController.navigateUp() }
        }
    }
}
