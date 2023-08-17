package com.example.braincard.database

import com.example.braincard.data.model.Card

class CardRepository(private val cardDao: CardDAO) {

    suspend fun getAllCards(): List<Card> {
        return cardDao.getAllCards()
    }

    suspend fun insertCard(card: Card) {
        cardDao.insertCard(card)
    }

    suspend fun deleteCard(cardId: String) {
        cardDao.deleteCard(cardId)
    }

    suspend fun updateCard(card: Card) {
        cardDao.updateCard(card)
    }

    suspend fun getCardById(cardId: String): Card {
        return cardDao.getCardByID(cardId)
    }
    suspend fun isCardIdInDatabase(cardId: String): Boolean {
        val allCards = cardDao.getAllCards()
        return allCards.any { card -> card.id == cardId }
    }
}


