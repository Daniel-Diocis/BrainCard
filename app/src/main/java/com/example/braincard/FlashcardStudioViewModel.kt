package com.example.braincard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FlashcardStudioViewModel(private val repository: CardRepository) : ViewModel() {
    /    // LiveData per i dettagli della carta.
    val cardLiveData = MutableLiveData<Card>()

    // Funzione per caricare una carta dal database dato il codice univoco
    fun loadCardByCode(cardCode: String) {
        viewModelScope.launch {
            val card = repository.getCardByCode(cardCode)
            cardLiveData.postValue(card)
        }

    }
    fun getFlashcardsByCodiceDeck(codiceDeck: String): LiveData<List<Card>> {
        return repository.getFlashcardsByCodiceDeck(codiceDeck)
    }
}
//TODO: Creare CardRepository e Model Card
