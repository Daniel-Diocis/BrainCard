package com.example.braincard


import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.braincard.data.model.Card
import com.example.braincard.data.model.Deck
import com.example.braincard.database.BrainCardDatabase
import com.example.braincard.database.CardRepository
import com.example.braincard.database.DeckRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FlashcardStudioViewModel(application : Application, deckId: String) : ViewModel() {
    // LiveData per i dettagli della carta.
    private val repository : CardRepository
    private val repository2 : DeckRepository
    lateinit var  gruppoId : MutableLiveData<String>
    lateinit var AllCard: LiveData<MutableList<Card>>
    val cardLiveData = MutableLiveData<Card>()
    lateinit var percentualeDeck : LiveData<Int>

    init {

        val deckDao= BrainCardDatabase.getDatabase(application).deckDao()
        val cardDao= BrainCardDatabase.getDatabase(application).cardDao()
        repository= CardRepository(cardDao)
        repository2=DeckRepository(deckDao)

        viewModelScope.launch {
            withContext(Dispatchers.IO){
                gruppoId = MutableLiveData(repository2.getDeckById(deckId).idGruppo)
                AllCard =  repository.getCardByDeckID(deckId)
                percentualeDeck=repository2.getPercentualeDeckByID(deckId)
                Log.e("Perc VIEWMODEL", percentualeDeck.value.toString())
            }
        }


    }


    // Funzione per caricare una carta dal database dato il codice univoco
    fun loadCardByCode(cardCode: String) {
        viewModelScope.launch() {
            val card = withContext(Dispatchers.IO){ repository.getCardById(cardCode)}
            cardLiveData.postValue(card)
            loadPercentualeDeck(card.deckID)

        }
    }



    fun loadPercentualeDeck(deckId: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                percentualeDeck = repository2.getPercentualeDeckByID(deckId)
            }
        }
    }


    fun getSizeOfDeck() : Int{
        return AllCard.value?.size ?: 0
    }


    fun updatePercentualeCompletamento(deckId : String, newPercentage : Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val deck = repository2.getDeckById(deckId)
                deck.percentualeCompletamento = newPercentage
                repository2.updateDeck(deck)
            }
        }
    }
    fun updateCompletataCard(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                cardLiveData.value?.completata = true
                cardLiveData.value?.let { repository.updateCard(it) }
            }
        }
    }

    fun updateSbagliataCard(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                if(cardLiveData.value?.completata == true){
                cardLiveData.value?.completata = false
                cardLiveData.value?.let { repository.updateCard(it)}
                }
            }
        }
    }
}



