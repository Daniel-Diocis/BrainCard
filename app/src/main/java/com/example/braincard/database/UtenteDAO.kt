package com.example.braincard.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.braincard.data.model.Card
import com.example.braincard.data.model.Utente
@Dao
interface UtenteDAO {
    @Query("SELECT * FROM Utente")
    fun getAllUtente(): LiveData<MutableList<Utente>>

    @Insert
    fun insertUtente(utente: Utente)

    @Delete
    fun deleteUtente(utente: Utente)

    @Update
    fun updateUtente(utente: Utente)

    @Query("SELECT * FROM Utente WHERE id== :utenteId ")
    fun getUtenteByID(utenteId: String) : Utente
}