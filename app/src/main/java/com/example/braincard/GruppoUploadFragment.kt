package com.example.braincard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.lifecycle.Observer
import com.example.braincard.data.model.Deck
import com.example.braincard.data.model.Gruppo
import com.example.braincard.database.DeckRepository
import com.example.braincard.databinding.FragmentGruppoDownloadBinding
import com.example.braincard.databinding.FragmentGruppoUploadBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GruppoUploadFragment : Fragment() {
    private var _binding: FragmentGruppoUploadBinding?=null

    private lateinit var viewModel: GruppoUploadViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGruppoUploadBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(GruppoUploadViewModel::class.java)
        val ContenitoreDecks = binding.gruppoUpload
        val gruppoIdSpecifico = arguments?.getString("gruppoShopLocale")
        Log.e("verifica",gruppoIdSpecifico.toString())


        val uploadButton = binding.uploadButton
        uploadButton.setOnClickListener {
            val selectedDecks = viewModel.getSelectedDecks()
            val currentGruppo = viewModel.getCurrentGruppo()

            // Ora puoi passare selectedDecks e currentGruppo al tuo database locale
            // Esegui le operazioni necessarie qui per salvare i dati nel tuo database
            if (currentGruppo != null) {
                uploadSelectedDecks(selectedDecks, currentGruppo)
            }
        }

        viewModel.deckGruppo.observe(viewLifecycleOwner, Observer { deckList ->
            // Pulisci la vista dei deck esistente, se necessario
            ContenitoreDecks.removeAllViews()

            // Ciclo attraverso i deck e crea le viste o elementi dell'interfaccia utente
            for (deck in deckList) {

                val checkBox = CheckBox(requireContext())
                checkBox.text = deck.nome

                // Aggiungi il button alla vista dei deck
                ContenitoreDecks.addView(checkBox)
            }
        })

        return binding.root
    }
    private fun uploadSelectedDecks(selectedDecks: List<Deck>, currentGruppo: Gruppo) {
        val db = FirebaseFirestore.getInstance()

        // Cicla sui deck selezionati
        for (deck in selectedDecks) {
            // Crea un riferimento al documento del deck su Firebase Firestore (ad esempio, utilizzando l'ID del deck)
            val deckRef = db.collection("Deck").document(deck.id)

            // Converti il deck in un oggetto mappa (Map) per caricarlo su Firestore
            val deckMap = mapOf(
                "nome" to deck.nome,
                "percentualeCompletamento" to deck.percentualeCompletamento,
                "gruppoId" to deck.idGruppo
                // Aggiungi altre proprietà se necessario
            )

            // Carica il deck su Firebase Firestore
            deckRef.set(deckMap)
                .addOnSuccessListener { _ ->
                    // Il deck è stato caricato con successo
                    // Puoi gestire la risposta qui se necessario
                }
                .addOnFailureListener { e ->
                    // Si è verificato un errore durante il caricamento del deck su Firestore
                    // Puoi gestire l'errore qui se necessario
                }
        }
    }

}