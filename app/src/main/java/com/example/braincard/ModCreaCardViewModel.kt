package com.example.braincard

import android.app.Application
import android.util.Log
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
import kotlin.properties.Delegates

class ModCreaCardViewModel(application: Application) : ViewModel() {

    private val repository: CardRepository
    private val repository2 : DeckRepository
    lateinit var  AllDeckCard : LiveData<MutableList<Card>>
    init {

        val cardDao= BrainCardDatabase.getDatabase(application).cardDao()
        val deckDao = BrainCardDatabase.getDatabase(application).deckDao()
        repository2= DeckRepository(deckDao)
        repository= CardRepository(cardDao)

    }

    fun getAllDeckCard(deckId: String) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                AllDeckCard = repository.getCardByDeckID(deckId)
            }}

    }
    // Funzione per caricare una carta dal database dato il codice univoco

    suspend fun isCardIdInDatabase(cardId: String): Boolean {
        return repository.isCardIdInDatabase(cardId)
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

        fun changePercentualeByDeckID(deckId: String, CardList : MutableList<Card>) {
            var completedCardCount = 0
            Log.e("Viewmodel", CardList.toString())
            CardList.forEach { card ->
                Log.e("DENTRO", card.toString())
                if (card.completata) {
                    Log.e("DENTRO","")
                    completedCardCount++
                }
            }
            Log.e("Viewmodel",completedCardCount.toString())

            val totalCardCount = CardList.size
            val newPercentage = (completedCardCount.toDouble() / totalCardCount * 100).toInt()
            Log.e("Viewmodel", newPercentage.toString())
            viewModelScope.launch { withContext(Dispatchers.IO){
                val deck = repository2.getDeckById(deckId)
                deck.percentualeCompletamento = newPercentage
                repository2.updateDeck(deck)
            }
        }
    }
    fun deleteCard(card : Card)
    {
        viewModelScope.launch{ withContext(Dispatchers.IO){repository.deleteCard(card)} }
    }
}
