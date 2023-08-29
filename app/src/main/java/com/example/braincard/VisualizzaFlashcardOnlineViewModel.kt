package com.example.braincard

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.braincard.data.model.Card
import com.google.firebase.firestore.FirebaseFirestore

class VisualizzaFlashcardOnlineViewModel(application: Application, deckId: String) : ViewModel() {

    lateinit var db : FirebaseFirestore
    var AllCards : MutableLiveData<MutableList<Card>> = MutableLiveData(mutableListOf())
    var cardLiveData = MutableLiveData<Card>()
    var list : MutableList<Card> = mutableListOf()

    init{

        db=FirebaseFirestore.getInstance()
        val collection = db.collection("Card")
        val cards = collection.whereEqualTo("deckId", deckId)
        cards.get().addOnSuccessListener { documents->

            for(document in documents) {
                list.add(Card(document.id, document.data["domanda"].toString(), document.data["risposta"].toString(), false,  document.data["deckId"].toString()))
            }
        }
        AllCards = MutableLiveData(mutableListOf())
    }
    fun createAllCards(){
        AllCards.postValue(list)
    }


}