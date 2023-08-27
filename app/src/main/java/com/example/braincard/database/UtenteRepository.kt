package com.example.braincard.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.braincard.data.model.Card
import com.example.braincard.data.model.Gruppo
import com.example.braincard.data.model.Utente

class UtenteRepository(private val utenteDAO: UtenteDAO) {
    val AllUtente: LiveData<MutableList<Utente>> = utenteDAO.getAllUtente()

    suspend fun insertUtente(utente: Utente) {
        utenteDAO.insertUtente(utente)
    }

    suspend fun deleteUtente(utenteId: Utente) {
        utenteDAO.deleteUtente(utenteId)
    }

    suspend fun updateUtente(utente: Utente) {
        utenteDAO.updateUtente(utente)
    }
}