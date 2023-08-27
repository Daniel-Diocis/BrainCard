package com.example.braincard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.braincard.database.UtenteDAO
import com.example.braincard.database.UtenteRepository
import com.example.braincard.databinding.FragmentModCreaCardBinding
import com.example.braincard.databinding.FragmentRegistrationBinding
import com.example.braincard.factories.FlashcardStudioViewModelFactory
import com.example.braincard.factories.RegistrationViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationFragment : Fragment() {

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    private lateinit var viewModel: RegistrationViewModel
    private lateinit var binding: FragmentRegistrationBinding
    private var utenteId: String = generateRandomString(20)
    private lateinit var nomeUtente: String
    private lateinit var password: String
    private lateinit var nome: String
    private lateinit var cognome: String
    private lateinit var email: String
    private lateinit var telefono: String
    private lateinit var genere: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(
            this, RegistrationViewModelFactory(requireActivity().application)
        ).get(RegistrationViewModel::class.java)
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSalvaUtente.setOnClickListener{
            nomeUtente = binding.nomeUtente.toString()
            password = binding.passwordd.toString()
            nome = binding.nome.toString()
            cognome = binding.cognome.toString()
            email = binding.email.toString()
            telefono = binding.telefono.toString()
            genere = binding.genere.toString()
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.registraUtente(utenteId, nomeUtente, password, nome, cognome, email, telefono, genere)
            }
            val bundle = bundleOf("utenteId" to utenteId)
            findNavController().navigate(R.id.action_registrationFragment_to_navigation_notifications, bundle)
        }
        // Ora puoi utilizzare il tuo view model
    }

    fun generateRandomString(length: Int): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

}