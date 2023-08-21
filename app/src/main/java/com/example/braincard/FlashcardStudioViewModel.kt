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
    val AllDeck : LiveData<List<Deck>>
    lateinit var AllCard : LiveData<MutableList<Card>>
    val cardLiveData = MutableLiveData<Card>()
    lateinit var percentualeDeck : LiveData<Int>

    init {

        val deckDao= BrainCardDatabase.getDatabase(application).deckDao()
        val cardDao= BrainCardDatabase.getDatabase(application).cardDao()
        repository= CardRepository(cardDao)
        repository2=DeckRepository(deckDao)
        AllDeck=repository2.AllDeck
        viewModelScope.launch {
            withContext(Dispatchers.Main){
                AllCard =  repository.getCardByDeckID(deckId)
                Log.e("AllCard VIEWMODEL", AllCard.value.toString())
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

    fun creaDeck (deckId: String){
        val deck = Deck(deckId, "DIO", 0, "09876543211098765432")
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repository2.insertDeck(deck)
                val card1= Card("12345678912345678999", "dominguez", "rispiii", false, deckId)
                val card2 = Card("12345678912345678988", "dom", "risp", false, deckId)
                repository.insertCard(card1)
                repository.insertCard(card2)
            }
        }

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
}



