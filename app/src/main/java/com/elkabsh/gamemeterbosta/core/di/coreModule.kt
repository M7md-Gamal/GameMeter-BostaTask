package com.elkabsh.gamemeterbosta.core.di

import com.elkabsh.gamemeterbosta.core.data.remote.HttpClientFactory
import org.koin.dsl.module

val coreModule = module {
    single {
        HttpClientFactory.create(get())
    }
}