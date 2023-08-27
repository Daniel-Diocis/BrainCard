package com.example.braincard.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Utente")
data class Utente(
    @PrimaryKey val id : String = "",
    val nomeUtente: String = "",
    val password: String = "",
    val nome: String = "",
    val cognome: String = "",
    val email: String = "",
    val telefono: String = "",
    val genere: String = ""
)