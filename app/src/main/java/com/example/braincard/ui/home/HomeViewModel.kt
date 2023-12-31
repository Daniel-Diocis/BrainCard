package com.example.braincard.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.braincard.data.model.Deck
import com.example.braincard.data.model.Gruppo
import com.example.braincard.database.BrainCardDatabase
import com.example.braincard.database.DeckRepository
import com.example.braincard.database.GruppoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GruppoRepository
    private val repositoryDeck:DeckRepository
     val AllGruppo:LiveData<List<Gruppo>>
     val HomeDeck:LiveData<List<Deck>>

    init {

        val gruppoDao=BrainCardDatabase.getDatabase(application).gruppoDao()
        val deckDao=BrainCardDatabase.getDatabase(application).deckDao()
        repository=GruppoRepository(gruppoDao )
        repositoryDeck= DeckRepository(deckDao)
        AllGruppo=repository.AllGruppo
        HomeDeck=repositoryDeck.HomeDeck
    }

    fun creaGruppo(nome:String){
        viewModelScope.launch(Dispatchers.IO) {
            val id=generateRandomString(20)
            Log.e("eccoloView",nome)
            val newGruppo=Gruppo(id,nome)
            repository.insertGruppo(newGruppo)
        }
        Log.e("controllo","fattogruèèpo")
    }
    fun eliminaGruppo(gruppo:Gruppo){

        viewModelScope.launch(Dispatchers.IO){
            repositoryDeck.deleteDeckByGruppoID(gruppo.id)
            repository.deleteGruppo(gruppo)
        }

    }
    fun generateRandomString(length: Int): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

}