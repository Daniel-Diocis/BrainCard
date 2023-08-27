package com.example.braincard.factories
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.braincard.RegistrationViewModel
import com.example.braincard.database.UtenteRepository

class RegistrationViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            return RegistrationViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}