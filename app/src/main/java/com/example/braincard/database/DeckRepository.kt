package com.example.braincard.database

import com.example.braincard.data.model.Card
import com.example.braincard.data.model.Deck

class DeckRepository(private val deckDao: DeckDAO) {

    // Metodi per interagire con la tabella dei mazzi (Deck)
    fun getAllDeck(): List<Deck> {
        return deckDao.getAllDeck()
    }

    fun insertDeck(deck: Deck) {
        deckDao.insertDeck(deck)
    }

    fun deleteDeck(deckId: Deck) {
        deckDao.deleteDeck(deckId)
    }

    fun updateDeck(deck: Deck) {
        deckDao.updateDeck(deck)
    }

    }







