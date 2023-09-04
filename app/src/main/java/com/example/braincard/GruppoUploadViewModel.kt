package com.example.braincard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.braincard.data.model.Deck
import com.example.braincard.data.model.Gruppo
import com.example.braincard.data.model.GruppoFire
import com.example.braincard.database.BrainCardDatabase
import com.example.braincard.database.GruppoDAO
import com.example.braincard.database.GruppoRepository
import com.google.firebase.firestore.FirebaseFirestore

class GruppoUploadViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var db : BrainCardDatabase
    var deckGruppo: MutableLiveData<List<Deck>> = MutableLiveData()
    var AllGruppi: MutableLiveData<MutableList<Gruppo>> = MutableLiveData(mutableListOf())
    var GruppiLocali: MutableList<Gruppo> =mutableListOf()
    val gruppoId = "CyX594EhY3BVZc9GWsrQ"
    private val selectedDecks: MutableList<Deck> = mutableListOf()
    private var currentGruppo: Gruppo? = null

    lateinit var GruppiOnline: LiveData<List<GruppoFire>>

    init {
        db = BrainCardDatabase.getDatabase(application)

    }
    fun creaGruppi() {
        AllGruppi.postValue(GruppiLocali)
    }

    // Metodo per ottenere i deck selezionati
    fun getSelectedDecks(): List<Deck> {
        return selectedDecks
    }

    // Metodo per aggiungere un deck selezionato
    fun addSelectedDeck(deck: Deck) {
        selectedDecks.add(deck)
    }

    // Metodo per rimuovere un deck selezionato
    fun removeSelectedDeck(deck: Deck) {
        selectedDecks.remove(deck)
    }

    // Metodo per impostare il gruppo corrente
    fun setCurrentGruppo(gruppo: Gruppo) {
        currentGruppo = gruppo
    }

    // Metodo per ottenere il gruppo corrente
    fun getCurrentGruppo(): Gruppo? {
        return currentGruppo
    }

    // ...
}