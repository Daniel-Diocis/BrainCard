package com.example.braincard.database

import androidx.lifecycle.LiveData
import com.example.braincard.data.model.Deck
import com.example.braincard.data.model.Gruppo

class GruppoRepository(private val gruppoDao: GruppoDAO) {
    val AllGruppo: LiveData<List<Gruppo>> = gruppoDao.getAllGruppo()


    suspend fun insertGruppo(gruppo: Gruppo) {
        gruppoDao.insertGruppo(gruppo)
    }

    suspend fun deleteGruppo(gruppoId: Gruppo) {
        gruppoDao.deleteGruppo(gruppoId)
    }

    suspend fun updateGruppo(gruppo: Gruppo) {
        gruppoDao.updateGruppo(gruppo )
    }

}