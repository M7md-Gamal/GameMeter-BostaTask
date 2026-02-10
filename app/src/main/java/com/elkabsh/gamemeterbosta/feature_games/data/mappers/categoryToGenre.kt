package com.elkabsh.gamemeterbosta.feature_games.data.mappers

import com.elkabsh.gamemeterbosta.feature_games.domain.model.GamesCategory

fun GamesCategory.toGenre(): String{
    return when (this) {
        GamesCategory.ACTION -> "action"
        GamesCategory.RPG -> "role-playing-games-rpg"
        GamesCategory.STRATEGY -> "strategy"
        GamesCategory.INDIE -> "indie"
        GamesCategory.SPORTS -> "sports"
    }
}