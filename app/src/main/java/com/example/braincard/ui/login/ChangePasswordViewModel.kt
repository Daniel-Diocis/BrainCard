package com.example.braincard.ui.login



import android.app.Application
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.braincard.R
import com.example.braincard.RegistrationFormState
import com.example.braincard.data.model.Utente
import com.example.braincard.database.BrainCardDatabase
import com.example.braincard.database.CardRepository
import com.example.braincard.database.DeckRepository
import com.example.braincard.database.UtenteRepository
import com.example.braincard.ui.login.LoginFormState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangePasswordViewModel(application: Application) : ViewModel() {

    lateinit private var repository: UtenteRepository
    val registrationFormState = MutableLiveData<RegistrationFormState>()
    private val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance().currentUser
    init {

        val utenteDAO= BrainCardDatabase.getDatabase(application).utenteDao()
        repository= UtenteRepository(utenteDAO)

    }
    fun registrationDataChanged3(passwordq1: String, password2: String) {
        if (!isPasswordValid(passwordq1)) {
            registrationFormState.value = RegistrationFormState(passwordError = R.string.invalid_password)
        }else if(!isPasswordValid(password2)) {
            registrationFormState.value = RegistrationFormState(passwordError2 = R.string.invalid_password)
        } else registrationFormState.value= RegistrationFormState(isDataValid = true)
    }
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }



}