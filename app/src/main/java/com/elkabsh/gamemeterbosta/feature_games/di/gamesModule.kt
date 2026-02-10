package com.elkabsh.gamemeterbosta.feature_games.di

import androidx.room.Room
import com.elkabsh.gamemeterbosta.feature_games.data.local.GamesDB
import com.elkabsh.gamemeterbosta.feature_games.data.remote.KtorRemoteGameDataSource
import com.elkabsh.gamemeterbosta.feature_games.data.remote.RemoteGameDataSource
import com.elkabsh.gamemeterbosta.feature_games.data.repo.GamesRepoImpl
import com.elkabsh.gamemeterbosta.feature_games.domain.repo.GamesRepo
import com.elkabsh.gamemeterbosta.feature_games.presentation.game_details.GameDetailsViewModel
import com.elkabsh.gamemeterbosta.feature_games.presentation.games_list.GamesListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val gamesModule = module {
    single<GamesRepo> { GamesRepoImpl(
        get(),
        gameDatabase = get()
    ) }
    single<RemoteGameDataSource> { KtorRemoteGameDataSource(get()) }
    single<GamesDB> {
        Room.databaseBuilder(
            context = get(),
            klass = GamesDB::class.java,
            name = "games_database.db"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }
    single { get<GamesDB>().gameDao() }
    single { get<GamesDB>().gameDetailsDao() }
    viewModelOf(::GamesListViewModel)
    viewModelOf(::GameDetailsViewModel)
}
