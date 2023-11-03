package com.example.braincard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.braincard.databinding.FragmentModprofiloBinding
import com.example.braincard.factories.ModProfiloViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore


class ModProfilo : Fragment() {

    companion object {
        fun newInstance() = ModProfilo()
    }

    private lateinit var viewModel: ModProfiloViewModel
    private lateinit var binding: FragmentModprofiloBinding
    private var utenteId: String = generateRandomString(20)
    private lateinit var nomeUtente: String
    private lateinit var nome: String
    private lateinit var cognome: String
    private lateinit var email: String
    private lateinit var telefono: String
    private lateinit var genere: String
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this,ModProfiloViewModelFactory(requireActivity().application)).get(ModProfiloViewModel::class.java)
        binding = FragmentModprofiloBinding.inflate(inflater, container, false)
        setup()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.registrationFormState.observe(viewLifecycleOwner,
            Observer { registrationFormState ->
                if (registrationFormState == null) {
                    return@Observer
                }
                binding.buttonSalvaUtente.isEnabled = registrationFormState.isDataValid
                registrationFormState.usernameError?.let {
                    binding.textNomeUtente.error = getString(it)
                }

                registrationFormState.telefonoError?.let {
                    binding.textTelefono.error = getString(it)
                }

            })

        binding.buttonSalvaUtente.setOnClickListener {
            nomeUtente = binding.textNomeUtente.text.toString()
            nome = binding.textNome.text.toString()
            cognome = binding.textCognome.text.toString()
            telefono = binding.textTelefono.text.toString()
            genere = binding.textGenere.selectedItem.toString()
            viewModel.updateUtente(nomeUtente,nome,cognome,telefono,genere)
            findNavController().navigate(R.id.action_modProfilo_to_navigation_notifications)
        }
        binding.button2.setOnClickListener{

            findNavController().navigate(R.id.action_modProfilo_to_changePassword)
        }

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.registrationDataChanged2(
                    binding.textNomeUtente.text.toString(),
                    binding.textTelefono.text.toString(),
                    binding.textGenere.selectedItem.toString(),

                )
            }
        }
        binding.textNomeUtente.addTextChangedListener(afterTextChangedListener)
        binding.textTelefono.addTextChangedListener(afterTextChangedListener)


    }


    fun generateRandomString(length: Int): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }
    fun setup(){
        binding.textNomeUtente.setText(arguments?.getString("nomeutente").toString())
        binding.textNome.setText(arguments?.getString("nome").toString())
        binding.textCognome.setText(arguments?.getString("cognome").toString())
        binding.textTelefono.setText(arguments?.getString("telefono").toString())
        var selectedItem = (arguments?.getString("genere").toString())

        val selectedPosition =
            (binding.textGenere.getAdapter() as ArrayAdapter<String?>).getPosition(selectedItem)
        binding.textGenere.setSelection(selectedPosition);
    }

}

