package com.example.braincard.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.braincard.data.model.Card
import com.example.braincard.data.model.Deck


@Database(entities = [Card::class, Deck::class], version = 1, exportSchema = false)
abstract class BrainCardDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDAO
    abstract fun deckDao(): DeckDAO
    companion object {
        @Volatile
        private var INSTANCE: BrainCardDatabase? = null

        fun getDatabase(context: Context): BrainCardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BrainCardDatabase::class.java,
                    "BrainCard"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}