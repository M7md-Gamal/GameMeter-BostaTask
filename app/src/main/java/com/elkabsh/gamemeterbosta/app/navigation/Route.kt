package com.elkabsh.gamemeterbosta.app.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object GamesList: Route
    @Serializable data class GameDetail(val id:Int): Route
}