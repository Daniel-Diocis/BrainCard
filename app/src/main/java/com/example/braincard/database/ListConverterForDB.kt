package com.example.braincard.database

import androidx.room.TypeConverter
import com.example.braincard.data.model.Card
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListConverterForDB {
    private val gson = Gson()

    @TypeConverter
    fun fromList(value: List<Card>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toList(value: String): List<Card> {
        val listType = object : TypeToken<List<Card>>() {}.type
        return gson.fromJson(value, listType)
    }
}
