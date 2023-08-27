package com.example.braincard

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.braincard.data.model.Utente
import com.example.braincard.database.BrainCardDatabase
import com.example.braincard.database.CardRepository
import com.example.braincard.database.DeckRepository
import com.example.braincard.database.UtenteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationViewModel(application: Application) : ViewModel() {

    lateinit private var repository: UtenteRepository
    init {

        val utenteDAO= BrainCardDatabase.getDatabase(application).utenteDao()
        repository= UtenteRepository(utenteDAO)

    }
    suspend fun registraUtente(utenteId: String, nomeUtente: String, password: String, nome: String, cognome: String, email: String, telefono: String, genere: String) {
        val nuovoUtente = Utente(
            utenteId, nomeUtente, password, nome, cognome, email, telefono, genere
        )
        // Passa l'oggetto nuovoUtente al repository per l'inserimento nel database
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUtente(nuovoUtente)
        }
    }



}