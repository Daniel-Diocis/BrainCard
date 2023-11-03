package com.example.braincard.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Card", foreignKeys = [ ForeignKey(entity = Deck::class, parentColumns = ["id"], childColumns = ["deckID"], onDelete = ForeignKey.CASCADE)])
data class Card (
    @PrimaryKey val id : String = "",
    var domanda : String = "",
    var risposta : String = "",
    var completata : Boolean = false,
    var deckID : String
)