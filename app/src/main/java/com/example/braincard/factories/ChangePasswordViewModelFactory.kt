package com.example.braincard.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.braincard.ModProfiloViewModel
import com.example.braincard.ui.login.ChangePasswordViewModel

class ChangePasswordViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChangePasswordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChangePasswordViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}