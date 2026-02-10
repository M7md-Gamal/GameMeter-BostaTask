package com.elkabsh.gamemeterbosta.core.di

import com.elkabsh.gamemeterbosta.core.data.remote.HttpClientFactory
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

val coreModule = module {
    single {
        HttpClientFactory.create(OkHttp.create ())
    }
}