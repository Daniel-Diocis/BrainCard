package com.example.braincard.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Deck")
data class Deck (
    @PrimaryKey val id : String = "",
    var percentualeCompletamento : Int = 0,
    var cards : List<Card>
)
