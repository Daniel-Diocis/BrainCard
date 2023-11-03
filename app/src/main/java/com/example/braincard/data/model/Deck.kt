package com.example.braincard.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Deck", foreignKeys =
    [ForeignKey(entity = Gruppo::class, parentColumns = ["id"], childColumns = ["idGruppo"], onDelete = ForeignKey.CASCADE)]
)
data class Deck (
    @PrimaryKey val id : String = "",
    var nome:String="",
    var percentualeCompletamento : Int = 0,
    var idGruppo:String=""


)
