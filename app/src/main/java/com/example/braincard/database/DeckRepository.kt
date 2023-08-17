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

    fun deleteDeck(deckId: String) {
        deckDao.deleteDeck(deckId)
    }

    fun updateDeck(deck: Deck) {
        deckDao.updateDeck(deck)
    }

    fun getCardsFromDeck(deckId: String): List<Card> {
        return deckDao.getCardsFromDeck(deckId)
    }
    fun findCardPositionInDeck(deckId: String, cardId: String): Int {
        val cards = deckDao.getCardsFromDeck(deckId)
        for ((index, card) in cards.withIndex()) {
            if (card.id == cardId) {
                return index
            }
        }
        return -1 // Carta non trovata
    }
    fun isCardIdInDeck(deckId: String, cardId: String): Boolean {
        val cards = deckDao.getCardsFromDeck(deckId)
        return cards.any { card -> card.id == cardId }
    }
    fun findDeckIdForCard(cardsList: List<Deck>, cardId: String): String? {
        for (deck in cardsList) {
            if (deck.cards.any { it.id == cardId }) {
                return deck.id
            }
        }
        return null
    }


}

