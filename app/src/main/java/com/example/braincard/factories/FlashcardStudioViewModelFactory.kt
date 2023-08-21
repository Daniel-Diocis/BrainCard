package com.example.braincard.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.braincard.FlashcardStudioViewModel

class FlashcardStudioViewModelFactory(private val application: Application, private val deckId : String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlashcardStudioViewModel::class.java)) {
            return FlashcardStudioViewModel(application, deckId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
