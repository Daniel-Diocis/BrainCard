package com.example.braincard.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.braincard.data.model.Card
import com.example.braincard.data.model.Deck


@Database(entities = [Card::class, Deck::class], version = 1, exportSchema = false)
abstract class BrainCardDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDAO
    abstract fun deckDao(): DeckDAO

}