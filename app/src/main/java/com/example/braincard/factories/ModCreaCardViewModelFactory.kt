package com.example.braincard.factories
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.braincard.ModCreaCardViewModel

class ModCreaCardViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModCreaCardViewModel::class.java)) {
            return ModCreaCardViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
