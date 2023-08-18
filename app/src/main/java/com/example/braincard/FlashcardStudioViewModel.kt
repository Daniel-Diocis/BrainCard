package com.example.braincard


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.braincard.data.model.Card
import com.example.braincard.database.CardRepository
import com.example.braincard.database.DeckRepository
import kotlinx.coroutines.launch

class FlashcardStudioViewModel(private val repository: CardRepository, private val repository2: DeckRepository) : ViewModel() {
    // LiveData per i dettagli della carta.
    val cardLiveData = MutableLiveData<Card>()
    val cardsLiveData = MutableLiveData<List<Card>>()

    // Funzione per caricare una carta dal database dato il codice univoco
    fun loadCardByCode(cardCode: String) {
        viewModelScope.launch {
            val card = repository.getCardById(cardCode)
            cardLiveData.postValue(card)
        }

    }
    fun getFlashcardsByCodiceDeck(codiceDeck: String): LiveData<List<Card>> {
        viewModelScope.launch {
            val cards = repository.getCardByDeckID(codiceDeck)
            cardsLiveData.postValue(cards)
        }
        return cardsLiveData
    }
}

