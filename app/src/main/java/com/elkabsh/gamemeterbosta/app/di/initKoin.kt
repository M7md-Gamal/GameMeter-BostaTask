package com.elkabsh.gamemeterbosta.app.di

import com.elkabsh.gamemeterbosta.core.di.coreModule
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration?=null) {
    startKoin {
        modules(coreModule)
        config?.invoke(this)
    }
}