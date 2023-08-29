package com.example.braincard.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.braincard.ModCreaCardViewModel
import com.example.braincard.VisualizzaFlashcardOnline
import com.example.braincard.VisualizzaFlashcardOnlineViewModel

class VisualizzaFlashcardOnlineViewModelFactory(private val application: Application, private val deckId : String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VisualizzaFlashcardOnlineViewModel::class.java)) {
            return VisualizzaFlashcardOnlineViewModel(application, deckId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}