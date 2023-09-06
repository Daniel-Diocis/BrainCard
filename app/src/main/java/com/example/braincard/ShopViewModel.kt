package com.example.braincard

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.braincard.data.model.Card
import com.example.braincard.data.model.Gruppo
import com.example.braincard.data.model.GruppoFire
import com.example.braincard.database.BrainCardDatabase
import com.example.braincard.database.GruppoRepository
import com.google.firebase.firestore.FirebaseFirestore

class ShopViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var db : FirebaseFirestore
    private val repository: GruppoRepository
    var AllGruppi: MutableLiveData<MutableList<GruppoFire>> = MutableLiveData(mutableListOf())
    var GruppiOnline: MutableList<GruppoFire> =mutableListOf()
    var GruppiCercati:MutableLiveData<MutableList<GruppoFire>> = MutableLiveData(mutableListOf())
    val GruppiOnlineCercati: MutableList<GruppoFire> =mutableListOf()

    lateinit var GruppiLocale:LiveData<List<Gruppo>>
    init {
        db=FirebaseFirestore.getInstance()

        val gruppiCollection = db.collection("Gruppo")

        gruppiCollection.get().addOnSuccessListener { documents->
            for(document in documents) {
                var utente= ""
                db.collection("Utente").document(document.data["utenteId"].toString()).get().addOnSuccessListener {ok->
                Log.e("ok3", ok.data?.get("displayName").toString())
                    utente= ok.data?.get("displayName").toString()
                    if(utente=="" || utente==null) utente="no name"
                    var grp=GruppoFire(document.data["nome"].toString(),utente,document.data["infoCreatore"].toString(),document.data["download"].toString(),document.id)
                    GruppiOnline.add(grp)
                    AllGruppi.postValue(GruppiOnline)

                }
                }


        }
        val gruppoDao= BrainCardDatabase.getDatabase(application).gruppoDao()
        repository=GruppoRepository(gruppoDao )
        GruppiLocale=repository.AllGruppo

    }
    fun cercaBar(query:String){

        val gruppiCollectionCerca = db.collection("Gruppo")
        GruppiOnlineCercati.clear()
        gruppiCollectionCerca.whereEqualTo("nome", query)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Esegui azioni sui documenti trovati qui

                    GruppiOnlineCercati.add(GruppoFire(document.data["nome"].toString(),document.data["infoCreatore"].toString(),document.data["download"].toString(),document.id))

                }
                Log.e("triavto",GruppiOnlineCercati.size.toString())
                GruppiCercati.postValue(GruppiOnlineCercati)
            }

    }
    fun creaGruppi(){
        AllGruppi.postValue(GruppiOnline)

    }
}