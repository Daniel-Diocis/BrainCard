package com.example.braincard

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.braincard.data.model.Card
import com.example.braincard.data.model.Deck
import com.example.braincard.data.model.Gruppo
import com.example.braincard.data.model.GruppoFire
import com.example.braincard.database.BrainCardDatabase
import com.example.braincard.database.CardRepository
import com.example.braincard.database.DeckRepository
import com.example.braincard.database.GruppoDAO
import com.example.braincard.database.GruppoRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GruppoUploadViewModel(application: Application, gruppoId : String) : AndroidViewModel(application) {
    lateinit var db : BrainCardDatabase
    lateinit var auth : FirebaseAuth
    var deckGruppo: LiveData<List<Deck>> = MutableLiveData()
    private var currentGruppoId = gruppoId
    lateinit var gruppoCorrente : Gruppo
    lateinit var AllCards : LiveData<MutableList<Card>>
    lateinit var cardCorrenti : MutableLiveData<MutableList<Card>>
    lateinit var repository: GruppoRepository
    lateinit var repository2: DeckRepository
    lateinit var repository3 : CardRepository
    var count = 0



    init {
        auth= FirebaseAuth.getInstance()
        db = BrainCardDatabase.getDatabase(application)
        repository = GruppoRepository(db.gruppoDao())
        repository2= DeckRepository(db.deckDao())
        repository3 = CardRepository(db.cardDao())
        viewModelScope.launch(Dispatchers.IO) {
            // Esegui la query sospesa direttamente in Dispatchers.IO
            val decks = repository2.getDeckByGruppoID(currentGruppoId)
            gruppoCorrente=repository.getGruppoById(currentGruppoId)
            // Ora puoi impostare i dati nel LiveData una volta che la query è completata
            deckGruppo = decks
            AllCards= repository3.getAllCards()

        }
        cardCorrenti=MutableLiveData(mutableListOf())

    }


    fun uploadSelectedDecks(selectedDecks: MutableList<Deck>, info : String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Gruppo").document(currentGruppoId).set(
            mapOf(
                "download" to 0,
                "infoCreatore" to info,
                "nome" to gruppoCorrente.nome,
                "utenteId" to auth.currentUser?.uid.toString()
            )
        )

        // Cicla sui deck selezionati
        Log.e("DECK SEL", selectedDecks.toString())
        for (deck in selectedDecks) {
            // Crea un riferimento al documento del deck su Firebase Firestore (ad esempio, utilizzando l'ID del deck)
            val deckRef = db.collection("Deck").document(deck.id)

            // Converti il deck in un oggetto mappa (Map) per caricarlo su Firestore
            val deckMap = mapOf(
                "nome" to deck.nome,
                "percentualeCompletamento" to deck.percentualeCompletamento,
                "gruppoId" to deck.idGruppo
            )
            // Carica il deck su Firebase Firestore
            deckRef.set(deckMap)
            viewModelScope.launch(Dispatchers.IO) {
                Log.e("prova", deck.id)
                Log.e("prova", cardCorrenti.value.toString())
                for (card in AllCards.value!!) {
                    if (card.deckID == deck.id) {
                        db.collection("Card").document(card.id).set(
                            mapOf(
                                "deckId" to deck.id,
                                "domanda" to card.domanda,
                                "risposta" to card.risposta,
                                "completata" to card.completata
                            )
                        )
                    }
            }

            }
        }
    }
    fun deleteSelectedDecks(deleteDecks : MutableList<Deck>){
        val db= FirebaseFirestore.getInstance()
        for(deck in deleteDecks){
            db.collection("Deck").document(deck.id).delete()

        }
        db.collection("Deck").whereEqualTo("gruppoId" ,deleteDecks[0].idGruppo).get().addOnSuccessListener { documents->
            Log.e("DOCUMENTO", documents.isEmpty.toString()+"----"+documents.toString())
            if (documents.isEmpty) db.collection("Gruppo").document(deleteDecks[0].idGruppo).delete()
        }
    }


}