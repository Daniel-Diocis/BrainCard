package com.example.braincard.ui.notifications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel(
    displayName: String,
    nome: String,
    cognome: String,
    email: String,
    telefono: String,
    genere: String
) : ViewModel() {

    val displayName = MutableLiveData<String>()
    val nome = MutableLiveData<String>()
    val cognome = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val telefono = MutableLiveData<String>()
    val genere = MutableLiveData<String>()

    init {
        // Inizializza i LiveData con i valori iniziali
        this.displayName.value = displayName
        this.nome.value = nome
        this.cognome.value = cognome
        this.email.value = email
        this.telefono.value = telefono
        this.genere.value = genere
    }
}
