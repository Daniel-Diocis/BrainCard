package com.example.braincard.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.braincard.ui.notifications.NotificationsViewModel

class NotificationsViewModelFactory(
    private val displayName: String,
    private val nome: String,
    private val cognome: String,
    private val email: String,
    private val telefono: String,
    private val genere: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationsViewModel(
                displayName,
                nome,
                cognome,
                email,
                telefono,
                genere
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
