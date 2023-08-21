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

class ModCreaCardViewModel(application: Application) : ViewModel() {

    private val repository: CardRepository
    private val repository2 : DeckRepository
    lateinit var  AllDeckCard : LiveData<MutableList<Card>>
    val cardLiveData = MutableLiveData<Card>()
    val cardIdLiveData=MutableLiveData<String>()
    init {

        val cardDao= BrainCardDatabase.getDatabase(application).cardDao()
        val deckDao = BrainCardDatabase.getDatabase(application).deckDao()
        repository2= DeckRepository(deckDao)
        repository= CardRepository(cardDao)

    }

    fun getAllDeckCard(deckId: String) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                AllDeckCard= repository.getCardByDeckID(deckId)
            }}

    }
    // Funzione per caricare una carta dal database dato il codice univoco
    fun loadCardByCode(cardCode: String)  {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val card = repository.getCardById(cardCode)
                cardLiveData.postValue(card)
                cardIdLiveData.postValue(card.id)
            }
        }
    }
    suspend fun isCardIdInDatabase(cardId: String): Boolean {
        return repository.isCardIdInDatabase(cardId)
    }
    suspend fun insertDeck(deck : Deck){
        return repository2.insertDeck(deck)
    }
    suspend fun insertCard(card : Card){
        return repository.insertCard(card)
    }
    suspend fun getCardById(cardId: String) : Card{
        return repository.getCardById(cardId)
    }
    suspend fun updateCard(card : Card){
        return repository.updateCard(card)
    }
    fun howManyInDeck(deckId : String): Int{
        var num : Int = 0
        viewModelScope.launch { withContext(Dispatchers.IO){ num =
            repository.getCardByDeckID(deckId).value?.size ?: 0
        }}
        return num
    }
}
//TODO: Creare CardRepository e Model Card