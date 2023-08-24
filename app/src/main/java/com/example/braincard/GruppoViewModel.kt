package com.example.braincard

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.braincard.data.model.Card
import com.example.braincard.data.model.Deck
import com.example.braincard.database.BrainCardDatabase
import com.example.braincard.database.CardRepository
import com.example.braincard.database.DeckRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GruppoViewModel(application: Application,gruppoid:String) : AndroidViewModel(application){
    private val repository: DeckRepository
    private val repository2 : CardRepository
    lateinit var AllDeck: LiveData<List<Deck>>


    var gruppoId:String=gruppoid



    init {


        val deckDao= BrainCardDatabase.getDatabase(application).deckDao()
        val cardDao= BrainCardDatabase.getDatabase(application).cardDao()
        repository= DeckRepository(deckDao)
        repository2 = CardRepository(cardDao)

        viewModelScope.launch{withContext(Dispatchers.Main) {

          AllDeck = repository.getDeckByGruppoID(gruppoid)
          Log.e("id", gruppoid)

         }
        }


    }




    fun creaDeck(){
        viewModelScope.launch(Dispatchers.IO) {
            val id=generateRandomString(20)
            val newDeck = Deck(id,"aldo",0,gruppoId) // Cambia i dettagli del nuovo deck
            repository.insertDeck(newDeck)

        }

        Log.e("controllo","fattoDeck")
    }
    fun generateRandomString(length: Int): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }
}
