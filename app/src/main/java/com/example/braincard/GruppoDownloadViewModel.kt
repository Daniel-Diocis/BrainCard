package com.example.braincard

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.braincard.data.model.Deck
import com.example.braincard.data.model.Gruppo
import com.example.braincard.data.model.GruppoFire
import com.example.braincard.database.BrainCardDatabase
import com.example.braincard.database.CardRepository
import com.example.braincard.database.DeckRepository
import com.example.braincard.database.GruppoRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GruppoDownloadViewModel(application: Application, gruppoid: String) : AndroidViewModel(application) {

    lateinit var db : FirebaseFirestore
    // Aggiungi un nuovo LiveData per i deck di un gruppo specifico
    private val repository: CardRepository
    private val repository2 : DeckRepository
    private val repository3 : GruppoRepository
    var deckGruppo: MutableLiveData<List<Deck>> = MutableLiveData()
    var AllGruppi: MutableLiveData<MutableList<GruppoFire>> = MutableLiveData(mutableListOf())
    var GruppiOnline: MutableList<GruppoFire> =mutableListOf()
    private val selectedDecks: MutableList<Deck> = mutableListOf()
    lateinit var gruppo: Gruppo

    lateinit var GruppiLocale: LiveData<List<Gruppo>>
    init {
        db= FirebaseFirestore.getInstance()

        val deckCollection = db.collection("Deck").whereEqualTo("gruppoId", gruppoid)

        val cardDao= BrainCardDatabase.getDatabase(application).cardDao()
        val deckDao = BrainCardDatabase.getDatabase(application).deckDao()
        val gruppoDao = BrainCardDatabase.getDatabase(application).gruppoDao()
        repository3= GruppoRepository(gruppoDao)
        repository2= DeckRepository(deckDao)
        repository= CardRepository(cardDao)

        val gruppo1 = db.collection("Gruppo").document(gruppoid)

        gruppo1.get().addOnSuccessListener { documents ->
            gruppo = Gruppo(gruppoid, documents.data!!["nome"].toString())
        }

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

    fun creaGruppi() {
        AllGruppi.postValue(GruppiOnline)
    }

    // Metodo per ottenere i deck selezionati
    fun getSelectedDecks(): List<Deck> {
        return selectedDecks
    }
    fun downloadSelectedDecks(selectedDecks: List<Deck>, currentGruppo: String) {
        // Qui dovresti implementare la logica per scaricare i deck selezionati.
        // Puoi utilizzare il tuo repository DeckRepository per inserire i deck nel database locale.
        Log.e("unMinimo", "")

            Log.e("dentroDownloadDecks", "")
            // Avvia una nuova coroutine
            viewModelScope.launch(Dispatchers.IO) {
                repository3.insertGruppo(gruppo)
                for (deck in selectedDecks) {
                    // Imposta l'ID del gruppo sul deck prima di salvarlo, se necessario
                    deck.idGruppo = currentGruppo
                    // Salva il deck nel database locale
                    repository2.insertDeck(deck)
            }
        }
    }

    // Metodo per aggiungere un deck selezionato
    fun addSelectedDeck(deck: Deck) {
        selectedDecks.add(deck)
    }

    // Metodo per rimuovere un deck selezionato
    fun removeSelectedDeck(deck: Deck) {
        selectedDecks.remove(deck)
    }

}