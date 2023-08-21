package com.example.braincard.database

import androidx.lifecycle.LiveData
import com.example.braincard.data.model.Card
import com.example.braincard.data.model.Deck

class DeckRepository(private val deckDao: DeckDAO) {

    // Metodi per interagire con la tabella dei mazzi (Deck)
    val AllDeck: LiveData<List<Deck>> = deckDao.getAllDeck()


    suspend fun insertDeck(deck: Deck) {
        deckDao.insertDeck(deck)
    }

    suspend fun deleteDeck(deckId: Deck) {
        deckDao.deleteDeck(deckId)
    }

    suspend fun updateDeck(deck: Deck) {
        deckDao.updateDeck(deck)
    }

    suspend fun getDeckById(deckId: String): Deck {
        return deckDao.getDeckByID(deckId)
    }
    suspend fun getDeckByGruppoID(gruppoId : String): LiveData<List<Deck>>{
        return deckDao.getDecksByGruppoID(gruppoId)
    }
    }







