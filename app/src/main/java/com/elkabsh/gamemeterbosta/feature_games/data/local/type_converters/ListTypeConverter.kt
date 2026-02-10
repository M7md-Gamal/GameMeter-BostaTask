package com.elkabsh.gamemeterbosta.feature_games.data.local.type_converters

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class ListTypeConverter {
    @TypeConverter
    fun fromStringList(value: String?): List<String> {
        if (value.isNullOrEmpty()) return emptyList()
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun toStringList(list: List<String>?): String {
        if (list.isNullOrEmpty()) return "[]"
        return Json.encodeToString(list)
    }
}