package com.elkabsh.gamemeterbosta.feature_games.domain.repo

import androidx.paging.PagingData
import com.elkabsh.gamemeterbosta.core.domain.Result
import com.elkabsh.gamemeterbosta.core.domain.errors.DataError
import com.elkabsh.gamemeterbosta.feature_games.domain.model.GameDetailItem
import com.elkabsh.gamemeterbosta.feature_games.domain.model.GameListItem
import com.elkabsh.gamemeterbosta.feature_games.domain.model.GamesCategory
import kotlinx.coroutines.flow.Flow

interface GamesRepo {
    fun getGamesByGenrePaged(
            category: GamesCategory,
            query: String = ""
    ): Flow<PagingData<GameListItem>>
    suspend fun getSingleGame(id: Int): Result<GameDetailItem, DataError>
}
