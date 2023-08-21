package com.example.braincard


import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.braincard.data.model.Card
import com.example.braincard.data.model.Deck
import com.example.braincard.database.BrainCardDatabase
import com.example.braincard.database.CardRepository
import com.example.braincard.database.DeckRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FlashcardStudioViewModel(application : Application) : ViewModel() {
    // LiveData per i dettagli della carta.
    private val repository : CardRepository
    private val repository2 : DeckRepository
    val AllDeck : LiveData<List<Deck>>
    lateinit var AllCard : MutableLiveData<MutableList<Card>>
    val cardLiveData = MutableLiveData<Card>()

    init {

        val deckDao= BrainCardDatabase.getDatabase(application).deckDao()
        val cardDao= BrainCardDatabase.getDatabase(application).cardDao()
        repository= CardRepository(cardDao)
        repository2=DeckRepository(deckDao)
        AllDeck=repository2.AllDeck

    }

    // Funzione per caricare una carta dal database dato il codice univoco
    fun loadCardByCode(cardCode: String) {
        viewModelScope.launch() {
            val card = withContext(Dispatchers.IO){ repository.getCardById(cardCode)}
            cardLiveData.postValue(card)
        }

    }

    fun getFlashcardsByCodiceDeck(codiceDeck: String): MutableLiveData<MutableList<Card>> {
        viewModelScope.launch {
            val cards = withContext(Dispatchers.IO) {
                repository.getCardByDeckID(codiceDeck)
            }
            AllCard.postValue(cards.value)
        }
        return AllCard

    }
}



