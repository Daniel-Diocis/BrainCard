package com.example.braincard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.braincard.data.model.Card
import com.example.braincard.database.CardRepository
import kotlinx.coroutines.launch

class ModCreaCardViewModel(private val repository: CardRepository) : ViewModel() {
    // LiveData per i dettagli della carta.
    val cardLiveData = MutableLiveData<Card>()
    val cardIdLiveData=MutableLiveData<String>()

    // Funzione per caricare una carta dal database dato il codice univoco
    fun loadCardByCode(cardCode: String)  {
        viewModelScope.launch {
            val card = repository.getCardById(cardCode)
            cardLiveData.postValue(card)
            cardIdLiveData.postValue(card.id)
        }
    }
    suspend fun isCardIdInDatabase(cardId: String): Boolean {
        return repository.isCardIdInDatabase(cardId)
    }
    fun howManyInDeck(deckId : String): Int{
        var num : Int = 0
        viewModelScope.launch {  num = repository.getCardByDeckID(deckId).size}
        return num
    }
}
//TODO: Creare CardRepository e Model Card