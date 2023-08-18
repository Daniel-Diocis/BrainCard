package com.example.braincard.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Card")
data class Card (
    @PrimaryKey val id : String = "",
    var domanda : String = "",
    var risposta : String = "",
    var completata : Boolean = false,
    var deckID : String
)