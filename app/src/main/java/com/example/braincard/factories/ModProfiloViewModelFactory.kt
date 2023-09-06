package com.example.braincard.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.braincard.ModProfiloViewModel

class ModProfiloViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModProfiloViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ModProfiloViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
