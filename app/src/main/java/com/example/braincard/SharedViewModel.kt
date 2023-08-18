package com.example.braincard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.braincard.database.BrainCardDatabase

class SharedViewModel(val appDatabase: BrainCardDatabase) : ViewModel() {

    private val _sharedVariable = MutableLiveData<String>()
    private val sharedVariable: LiveData<String> = _sharedVariable

    fun updateSharedVariable(newValue: String) {
        _sharedVariable.value = newValue
    }
    fun getAppDB() : LiveData<String>{
        return sharedVariable
    }

}
