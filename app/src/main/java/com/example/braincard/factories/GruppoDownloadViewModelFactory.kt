package com.example.braincard.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.braincard.GruppoDownloadViewModel
import com.example.braincard.GruppoViewModel

class GruppoDownloadViewModelFactory(private val application: Application, private val gruppoid:String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GruppoDownloadViewModel::class.java)) {
            return GruppoDownloadViewModel(application, gruppoid) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}