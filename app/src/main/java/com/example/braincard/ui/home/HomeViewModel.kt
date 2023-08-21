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
     val AllGruppo:LiveData<List<Gruppo>>

    init {

        val gruppoDao=BrainCardDatabase.getDatabase(application).gruppoDao()
        repository=GruppoRepository(gruppoDao )
        AllGruppo=repository.AllGruppo
    }

    fun creaGruppo(){
        viewModelScope.launch(Dispatchers.IO) {
            val id=generateRandomString(20)
            val newGruppo=Gruppo(id,"Geografia",)
            repository.insertGruppo(newGruppo)
        }
        Log.e("controllo","fattogruèèpo")
    }
    fun generateRandomString(length: Int): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

}