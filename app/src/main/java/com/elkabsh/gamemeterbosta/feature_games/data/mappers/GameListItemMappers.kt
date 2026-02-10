package com.elkabsh.gamemeterbosta.feature_games.data.mappers

import com.elkabsh.gamemeterbosta.feature_games.data.local.entity.GameEntity
import com.elkabsh.gamemeterbosta.feature_games.data.remote.dto.list_of_games_dto.GameDto
import com.elkabsh.gamemeterbosta.feature_games.domain.model.GameListItem

fun GameEntity.toGameListItem() =
        GameListItem(
                id = id,
                name = name,
                releaseDate = releaseDate,
                backgroundImage = backgroundImage,
                rating = rating
        )

fun GameDto.toGameEntity() =
        GameEntity(
                id = id,
                name = name,
                releaseDate = released,
                backgroundImage = backgroundImage,
                rating = rating,
                category = category.map { it.name },
        )
