package com.elkabsh.gamemeterbosta.feature_games.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.elkabsh.gamemeterbosta.feature_games.data.local.dao.GameDao
import com.elkabsh.gamemeterbosta.feature_games.data.local.dao.GameDetailsDao
import com.elkabsh.gamemeterbosta.feature_games.data.local.dao.GameRemoteKeysDao
import com.elkabsh.gamemeterbosta.feature_games.data.local.entity.GameDetailsEntity
import com.elkabsh.gamemeterbosta.feature_games.data.local.entity.GameEntity
import com.elkabsh.gamemeterbosta.feature_games.data.local.entity.GameRemoteKeys
import com.elkabsh.gamemeterbosta.feature_games.data.local.type_converters.ListTypeConverter

@Database(
        entities = [GameEntity::class, GameDetailsEntity::class, GameRemoteKeys::class],
        version = 2
)
@TypeConverters(ListTypeConverter::class)
abstract class GamesDB : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun gameDetailsDao(): GameDetailsDao
    abstract fun gameRemoteKeysDao(): GameRemoteKeysDao
}
