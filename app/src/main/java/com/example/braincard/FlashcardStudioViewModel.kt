package com.example.braincard

import CardRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.braincard.data.model.Card
import kotlinx.coroutines.launch

class FlashcardStudioViewModel(private val repository: CardRepository) : ViewModel() {
    // LiveData per i dettagli della carta.
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
// TODO: Creare CardRepository e Model Card codice, codiceDeck, domanda, risposta, numero
// TODO: Model Deck-> codiceDeck, numeroCarteTot, punteggio, completato
// TODO: Model Utente->User,PSW,...
