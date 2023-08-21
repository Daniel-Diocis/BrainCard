package com.example.braincard.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Gruppo")
data class Gruppo (
    @PrimaryKey val id : String = "",
    val nome:String="",

    )