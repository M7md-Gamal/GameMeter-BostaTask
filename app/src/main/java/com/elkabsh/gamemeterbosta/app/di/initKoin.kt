package com.elkabsh.gamemeterbosta.app.di

import com.elkabsh.gamemeterbosta.core.di.coreModule
import com.elkabsh.gamemeterbosta.feature_games.di.gamesModule
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        modules(coreModule, gamesModule)
        config?.invoke(this)
    }
}
