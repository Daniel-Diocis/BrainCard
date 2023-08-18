package com.example.braincard.database

import com.example.braincard.data.model.Card

class CardRepository(private val cardDao: CardDAO) {

    suspend fun getAllCards(): List<Card> {
        return cardDao.getAllCards()
    }

    suspend fun insertCard(card: Card) {
        cardDao.insertCard(card)
    }

    suspend fun deleteCard(card: Card) {
        cardDao.deleteCard(card)
    }

    suspend fun updateCard(card: Card) {
        cardDao.updateCard(card)
    }

    suspend fun getCardById(cardId: String): Card {
        return cardDao.getCardByID(cardId)
    }
    suspend fun getCardByDeckID(deckId : String): List<Card>{
        return cardDao.getCardsByDeckID(deckId)
    }
    suspend fun isCardIdInDatabase(cardId: String): Boolean {
        val bool = cardDao.getCardByID(cardId) != null
        return bool

    }
}


