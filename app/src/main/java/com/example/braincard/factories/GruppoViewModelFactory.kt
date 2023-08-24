package com.example.braincard.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.braincard.GruppoViewModel

class GruppoViewModelFactory(private val application: Application, private val gruppoid:String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GruppoViewModel::class.java)) {
            return GruppoViewModel(application, gruppoid) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
