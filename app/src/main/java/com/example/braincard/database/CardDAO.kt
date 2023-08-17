package com.example.braincard.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.braincard.data.model.Card

@Dao
interface CardDAO {
    @Query("SELECT * FROM Card")
    fun getAllCards(): List<Card>

    @Insert
    fun insertCard(card: Card)

    @Delete
    fun deleteCard(cardId : String)

    @Update
    fun updateCard(card: Card)

    @Query("SELECT * FROM Card WHERE id== :cardId ")
    fun getCardByID(cardId: String) : Card

}