package com.example.braincard

import androidx.lifecycle.MutableLiveData

class PopUpMessage private constructor() {
    var messageLiveData=MutableLiveData<String>()



    // ... altri metodi e proprietà

    companion object {
        @Volatile
        private var instance: PopUpMessage? = null

        fun getInstance(): PopUpMessage {
            return instance ?: synchronized(this) {
                instance ?: PopUpMessage().also { instance = it }
            }
        }
    }
}