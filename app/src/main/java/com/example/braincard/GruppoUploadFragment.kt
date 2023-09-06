package com.example.braincard

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.braincard.data.model.Deck
import com.example.braincard.databinding.FragmentGruppoUploadBinding
import com.example.braincard.factories.GruppoUploadViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class GruppoUploadFragment : Fragment() {
    private var _binding: FragmentGruppoUploadBinding?=null
    private var progressDialog: ProgressDialog? = null
    private lateinit var viewModel: GruppoUploadViewModel
    private val binding get() = _binding!!
    var selectedDecks: MutableList<Deck> = mutableListOf()
    var deleteDecks: MutableList<Deck> = mutableListOf()
    var gruppoIdSpecifico : String = ""
    val db= FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("AndroidRuntime", gruppoIdSpecifico+":::::: "+arguments?.getString("gruppoId").toString())
        gruppoIdSpecifico = arguments?.getString("gruppoId").toString()
        _binding = FragmentGruppoUploadBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, GruppoUploadViewModelFactory(requireActivity().application, gruppoIdSpecifico)).get(GruppoUploadViewModel::class.java)
        val auth = FirebaseAuth.getInstance()


        val uploadButton = binding.uploadButton
        binding.creatorName.text = auth.currentUser?.displayName


        uploadButton.setOnClickListener { if (selectedDecks.isNotEmpty())
            viewModel.uploadSelectedDecks(selectedDecks,binding.shortEditMessage.text.toString())
            Log.e("DELETING",deleteDecks.isNotEmpty().toString())
            if(deleteDecks.isNotEmpty()) viewModel.deleteSelectedDecks(deleteDecks)
            findNavController().navigate(R.id.action_gruppoUploadFragment_to_navigation_dashboard)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ContenitoreDecks = binding.gruppoUpload
        viewModel.deckGruppo.observe(viewLifecycleOwner, Observer { deckList ->

                if(deckList!=null) {
                    Log.e("INSIDE",deckList.toString())
                    ContenitoreDecks.removeAllViews()
                    var count = 0
                    showLoadingScreen()
                    for (deck in deckList) {

                        val checkBox = CheckBox(requireContext())
                        checkDeckOnline(deck.id) { exists ->
                            if (exists) checkBox.isChecked = true
                        }
                        checkBox.text = deck.nome
                        // Aggiungi il button alla vista dei deck
                        ContenitoreDecks.addView(checkBox)
                        checkBox.setOnClickListener {
                            if (checkBox.isChecked) {
                                selectedDecks.add(deck)
                                for (item in deleteDecks) {
                                    if (item.id == deck.id) {
                                        deleteDecks.remove(deck)
                                        break
                                    }
                                }
                            } else {
                                deleteDecks.add(deck)
                                for (item in selectedDecks) {
                                    if (item.id == deck.id) {
                                        selectedDecks.remove(deck)
                                        break
                                    }
                                }
                            }
                        }
                        count++
                        if (count == deckList.size) hideLoadingScreen()
                    }
                }
            })


    }
        fun checkDeckOnline(deckId: String, callback: (Boolean) -> Unit) {
            db.collection("Deck").document(deckId).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        val exists = document.exists()
                        callback(exists)
                    } else {
                        callback(false) // Segnala che il documento non esiste a causa di un errore
                    }
                }

    }
    // Funzione per mostrare la schermata di caricamento
    fun showLoadingScreen() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(requireContext())
            progressDialog?.setMessage("Caricamento in corso...") // Imposta un messaggio di caricamento
            progressDialog?.setCancelable(false)
            progressDialog?.show()
        }
    }

    // Funzione per nascondere la schermata di caricamento
    fun hideLoadingScreen() {
        progressDialog?.dismiss()
        progressDialog = null
    }


}