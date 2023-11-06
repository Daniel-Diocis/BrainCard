package com.example.braincard

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.braincard.data.model.Card
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
import kotlinx.coroutines.withContext

class GruppoDownloadViewModel(application: Application, gruppoid: String) : AndroidViewModel(application) {

    lateinit var db : FirebaseFirestore
    val idgruppo=gruppoid
    private val repository: CardRepository
    private val repository2 : DeckRepository
    private val repository3 : GruppoRepository
    lateinit var AllCards : LiveData<MutableList<Card>>
    var deckGruppo: MutableLiveData<List<Deck>> = MutableLiveData()
    var AllGruppi: LiveData<List<Gruppo>> = MutableLiveData(mutableListOf())
    var GruppiOnline: MutableList<GruppoFire> =mutableListOf()
    private val selectedDecks: MutableList<Deck> = mutableListOf()
    lateinit var gruppo: Gruppo
    lateinit var deckInRoom : LiveData<List<Deck>>

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
        viewModelScope.launch(Dispatchers.IO) {
             deckInRoom = repository2.getDeckByGruppoID(idgruppo)
            AllGruppi= repository3.getAllGruppi()
        }

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


    // Metodo per ottenere i deck selezionati
    fun getSelectedDecks(): List<Deck> {
        return selectedDecks
    }
    fun downloadSelectedDecks(selectedDecks: List<Deck>, currentGruppo: String) {
        var trovato = false
        for (gruppo in AllGruppi.value!!){
            if (gruppo.id == currentGruppo) {
                trovato = true
                break
            }}
        if (!trovato) viewModelScope.launch(Dispatchers.IO) { repository3.insertGruppo(gruppo)}
            for (deck in selectedDecks) {
                // Imposta l'ID del gruppo sul deck prima di salvarlo
                deck.idGruppo = currentGruppo
                deck.percentualeCompletamento = 0
                // Salva il deck nel database locale
                viewModelScope.launch(Dispatchers.IO){ repository2.insertDeck(deck) }
                val cards = db.collection("Card").whereEqualTo("deckId", deck.id)
                cards.get().addOnSuccessListener { Cards->
                    for (card in Cards) {
                        if (card.data["deckId"].toString()==deck.id) {
                            val cardDaIns= Card(
                                id = card.id,
                                deckID = card.data["deckId"].toString(),
                                domanda = card.data["domanda"].toString(),
                                risposta = card.data["risposta"].toString(),
                                completata = false
                            )
                            viewModelScope.launch(Dispatchers.IO) {
                                repository.insertCard(cardDaIns)
                            }
                        }
                }
                }
            }
            db.collection("Gruppo").document(currentGruppo).get().addOnSuccessListener { task ->
                val currentDownload = task.data?.get("download") as Long
                val newDownload = currentDownload + 1

                val updateData = hashMapOf(
                    "download" to newDownload
                )

                db.collection("Gruppo").document(currentGruppo).update(updateData as Map<String, Any>)
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