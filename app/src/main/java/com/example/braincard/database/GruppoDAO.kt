package com.example.braincard.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

import com.example.braincard.data.model.Gruppo

@Dao
interface GruppoDAO {
    @Query("SELECT * FROM GRUPPO")
    fun getAllGruppi(): LiveData<List<Gruppo>>


    @Insert
    fun insertGruppo(gruppo: Gruppo)

    @Delete
    fun deleteGruppo(gruppo: Gruppo)

    @Update
    fun updateGruppo(gruppo: Gruppo)

    @Query("SELECT * FROM Gruppo WHERE id = :gruppoId")
    fun getGruppoById(gruppoId : String) : Gruppo
}