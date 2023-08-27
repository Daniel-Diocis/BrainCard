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
import androidx.appcompat.app.AppCompatActivity
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

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSalvaUtente.setOnClickListener {
            nomeUtente = binding.textNomeUtente.text.toString()
            password = binding.editTextPassword.text.toString()
            nome = binding.textNome.text.toString()
            cognome = binding.textCognome.text.toString()
            email = binding.textEmail.text.toString()
            telefono = binding.textTelefono.text.toString()
            genere = binding.textGenere.text.toString()
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.registraUtente(
                    utenteId,
                    nomeUtente,
                    password,
                    nome,
                    cognome,
                    email,
                    telefono,
                    genere
                )
            }
            val bundle = bundleOf("utenteId" to utenteId)
            findNavController().navigate(
                R.id.action_registrationFragment_to_navigation_notifications,
                bundle
            )
        }
    }

    fun generateRandomString(length: Int): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

}