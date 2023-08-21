package com.example.braincard.database

import androidx.lifecycle.LiveData
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
    fun getAllDeck(): LiveData<List<Deck>>

    @Insert
    fun insertDeck(deck: Deck)

    @Delete
    fun deleteDeck(deck : Deck)

    @Update
    fun updateDeck(deck: Deck)
    @Query("SELECT * FROM Deck WHERE id== :deckId ")
    fun getDeckByID(deckId: String) : Deck

    @Query("SELECT * FROM Deck WHERE idGruppo== :gruppoId")
    fun getDecksByGruppoID(gruppoId : String) : List<Deck>




}