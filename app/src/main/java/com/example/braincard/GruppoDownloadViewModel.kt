package com.example.braincard

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.braincard.data.model.Deck
import com.example.braincard.data.model.Gruppo
import com.example.braincard.data.model.GruppoFire
import com.example.braincard.database.BrainCardDatabase
import com.example.braincard.database.GruppoRepository
import com.google.firebase.firestore.FirebaseFirestore

class GruppoDownloadViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var db : FirebaseFirestore
    // Aggiungi un nuovo LiveData per i deck di un gruppo specifico
    var deckGruppo: MutableLiveData<List<Deck>> = MutableLiveData()
    var AllGruppi: MutableLiveData<MutableList<GruppoFire>> = MutableLiveData(mutableListOf())
    var GruppiOnline: MutableList<GruppoFire> =mutableListOf()
    val gruppoId = "5NKoqNeN7Pis9Vrh2SCe"

    lateinit var GruppiLocale: LiveData<List<Gruppo>>
    init {
        db= FirebaseFirestore.getInstance()

        val deckCollection = db.collection("Deck").whereEqualTo("gruppoId", gruppoId)

        deckCollection.get().addOnSuccessListener { documents ->
            Log.e("gruppo", "Numero di documenti: ${documents.size()}")
            val deckList = mutableListOf<Deck>()
            for (document in documents) {
                val deck = Deck(
                    id = document.id,
                    nome = document.data["nome"].toString(),
                    percentualeCompletamento = document.data["percentualeCompletamento"].toString().toInt(),
                    idGruppo = document.data["gruppoId"].toString()
                )
                deckList.add(deck)
            }
            deckGruppo.postValue(deckList)
        }

    }

    // Aggiungi un metodo per caricare i deck di un gruppo specifico
    fun caricaDeckPerGruppo(gruppoId: String) {
        val deckCollection = db.collection("Deck").whereEqualTo("idGruppo", gruppoId)

        deckCollection.get().addOnSuccessListener { documents ->
            val deckList = mutableListOf<Deck>()
            for (document in documents) {
                val deck = Deck(
                    id = document.id,
                    nome = document.data["nome"].toString(),
                    percentualeCompletamento = document.data["percentualeCompletamento"].toString().toInt(),
                    idGruppo = document.data["idGruppo"].toString()
                )
                deckList.add(deck)
            }
            deckGruppo.postValue(deckList)
        }
    }
    fun creaGruppi() {
        AllGruppi.postValue(GruppiOnline)
    }
}