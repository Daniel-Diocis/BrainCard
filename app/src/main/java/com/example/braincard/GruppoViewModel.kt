package com.example.braincard

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.braincard.data.model.Deck
import com.example.braincard.database.BrainCardDatabase
import com.example.braincard.database.DeckRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GruppoViewModel(application: Application) : AndroidViewModel(application){
    private val repository: DeckRepository
    val AllDeck: LiveData<List<Deck>>

    init {

        val deckDao= BrainCardDatabase.getDatabase(application).deckDao()
        repository= DeckRepository(deckDao)
        AllDeck=repository.AllDeck
    }
    fun creaDeck(){
        viewModelScope.launch(Dispatchers.IO) {
            val newDeck = Deck("abCDE678901234567890",0) // Cambia i dettagli del nuovo deck
            repository.insertDeck(newDeck)
        }
        Log.e("controllo","fattoDeck")
    }
}