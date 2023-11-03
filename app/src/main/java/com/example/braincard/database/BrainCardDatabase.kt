package com.example.braincard.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.braincard.data.model.Card
import com.example.braincard.data.model.Deck
import com.example.braincard.data.model.Gruppo
import com.example.braincard.data.model.Utente

@Database(entities = [Card::class, Deck::class,Gruppo::class, Utente::class], version = 6, exportSchema = false)
abstract class BrainCardDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDAO
    abstract fun deckDao(): DeckDAO
    abstract fun gruppoDao(): GruppoDAO
    abstract fun utenteDao(): UtenteDAO
    companion object {
        @Volatile
        private var INSTANCE: BrainCardDatabase? = null

        fun getDatabase(context: Context): BrainCardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BrainCardDatabase::class.java,
                    "BrainCard"
                )
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }

}