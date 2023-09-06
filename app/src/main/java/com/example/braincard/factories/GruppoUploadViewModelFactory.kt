package com.example.braincard.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.braincard.GruppoUploadViewModel

class GruppoUploadViewModelFactory(private val application: Application, private val gruppoId: String) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GruppoUploadViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GruppoUploadViewModel(application, gruppoId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}