package com.example.braincard.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.braincard.FlashcardStudioViewModel
import com.example.braincard.database.CardRepository
import com.example.braincard.database.DeckRepository

class FlashcardStudioViewModelFactory(private val cardRepository: CardRepository, private val deckRepository: DeckRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlashcardStudioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FlashcardStudioViewModel(cardRepository, deckRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
