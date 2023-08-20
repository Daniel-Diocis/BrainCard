package com.example.braincard.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.braincard.data.model.Deck
import com.example.braincard.database.BrainCardDatabase
import com.example.braincard.database.DeckRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository:DeckRepository
     val AllDeck:LiveData<List<Deck>>

    init {

        val deckDao=BrainCardDatabase.getDatabase(application).deckDao()
        repository=DeckRepository(deckDao)
        AllDeck=repository.AllDeck
    }


}