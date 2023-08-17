package com.example.braincard.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.braincard.data.model.Card
import com.example.braincard.data.model.Deck

@Dao
interface DeckDAO {
    @Query("SELECT * FROM DECK")
    fun getAllDeck(): List<Deck>

    @Insert
    fun insertDeck(deck: Deck)

    @Delete
    fun deleteDeck(deckId : String)

    @Update
    fun updateDeck(deck: Deck)

    @Query("SELECT cards FROM Deck WHERE id== :deckId ")
    fun getCardsFromDeck(deckId: String) : List<Card>

}