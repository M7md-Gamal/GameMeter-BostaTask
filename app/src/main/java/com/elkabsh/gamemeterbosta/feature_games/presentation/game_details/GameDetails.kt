package com.elkabsh.gamemeterbosta.feature_games.presentation.game_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import com.elkabsh.gamemeterbosta.feature_games.presentation.game_details.components.AboutSection
import com.elkabsh.gamemeterbosta.feature_games.presentation.game_details.components.GallerySection
import com.elkabsh.gamemeterbosta.feature_games.presentation.game_details.components.GameInfoSection
import com.elkabsh.gamemeterbosta.feature_games.presentation.game_details.components.HeroImageSection
import com.elkabsh.gamemeterbosta.ui.theme.GameMeterBostaTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GameDetailsScreen(
    viewModel: GameDetailsViewModel = koinViewModel(), onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(true) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                else -> {}
            }
        }
    }

    GameDetailsContent(
        state = state, onAction = viewModel::onAction, onNavigateBack = onNavigateBack
    )
}

@Composable
fun GameDetailsContent(
    state: GameDetailsState,
    onAction: (GameDetailsAction) -> Unit,
    onNavigateBack: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = state.error.asString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                // Hero Image Section
                HeroImageSection(
                    gameName = state.gameName,
                    gameImgUrl = state.gameImg,
                    onNavigateBack = onNavigateBack
                )
                // Game Info Section
                GameInfoSection(
                    gameRating = state.gameRating,
                    gameReleaseDate = state.gameReleaseDate,
                    gameGenre = state.gameGenre,
                    modifier = Modifier.padding(24.dp)
                )
                // About Section
                AboutSection(
                    description = state.gameDescription,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                )
                // Gallery Section
                GallerySection(
                    gameScreenShots = state.gameScreenshots,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

        }
    }
}


@Preview
@Composable
private fun Preview() {
    GameMeterBostaTheme {
        GameDetailsContent(
            state = GameDetailsState(
                gameName = "Grand Theft Auto V",
                gameDescription = "<p>Rockstar Games went bigger, since their previous installment of the series. You get the complicated and realistic world-building from Liberty City of GTA4 in the setting of lively and diverse Los Santos, from an old fan favorite GTA San Andreas. 561 different vehicles (including every transport you can operate) and the amount is rising with every update. <br />\nSimultaneous storytelling from three unique perspectives: <br />\nFollow Michael, ex-criminal living his life of leisure away from the past, Franklin, a kid that seeks the better future, and Trevor, the exact past Michael is trying to run away from. <br />\nGTA Online will provide a lot of additional challenge even for the experienced players, coming fresh from the story mode. Now you will have other players around that can help you just as likely as ruin your mission. Every GTA mechanic up to date can be experienced by players through the unique customizable character, and community content paired with the leveling system tends to keep everyone busy and engaged.</p>\n<p>Español<br />\nRockstar Games se hizo más grande desde su entrega anterior de la serie. Obtienes la construcción del mundo complicada y realista de Liberty City de GTA4 en el escenario de Los Santos, un viejo favorito de los fans, GTA San Andreas. 561 vehículos diferentes (incluidos todos los transportes que puede operar) y la cantidad aumenta con cada actualización.<br />\nNarración simultánea desde tres perspectivas únicas:<br />\nSigue a Michael, ex-criminal que vive su vida de ocio lejos del pasado, Franklin, un niño que busca un futuro mejor, y Trevor, el pasado exacto del que Michael está tratando de huir.<br />\nGTA Online proporcionará muchos desafíos adicionales incluso para los jugadores experimentados, recién llegados del modo historia. Ahora tendrás otros jugadores cerca que pueden ayudarte con la misma probabilidad que arruinar tu misión. Los jugadores pueden experimentar todas las mecánicas de GTA actualizadas a través del personaje personalizable único, y el contenido de la comunidad combinado con el sistema de nivelación tiende a mantener a todos ocupados y comprometidos.</p>",
                gameReleaseDate = "2013-09-17",
                gameRating = "4.47",
                gameGenre = listOf("Action"),
                gameScreenshots = listOf(
                    "https://media.rawg.io/media/screenshots/a7c/a7c43871a54bed6573a6a429451564ef.jpg",
                    "https://media.rawg.io/media/screenshots/cf4/cf4367daf6a1e33684bf19adb02d16d6.jpg",
                    "https://media.rawg.io/media/screenshots/f95/f9518b1d99210c0cae21fc09e95b4e31.jpg",
                    "https://media.rawg.io/media/screenshots/a5c/a5c95ea539c87d5f538763e16e18fb99.jpg",
                    "https://media.rawg.io/media/screenshots/a7e/a7e990bc574f4d34e03b5926361d1ee7.jpg",
                ),
                gameImg = "https://media.rawg.io/media/games/20a/20aa03a10cda45239fe22d035c0ebe64.jpg"

            ), onAction = {}
        )
    }
}
