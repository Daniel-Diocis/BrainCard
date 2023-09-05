package com.example.braincard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.braincard.Adattatori.BannerAdapter
import com.example.braincard.data.model.Deck
import com.example.braincard.data.model.Gruppo
import com.example.braincard.database.BrainCardDatabase
import com.example.braincard.database.DeckRepository
import com.example.braincard.databinding.FragmentGruppoDownloadBinding
import com.example.braincard.databinding.FragmentShopBinding
import com.example.braincard.factories.GruppoDownloadViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Gruppo_DownloadFragment : Fragment() {
    private var _binding: FragmentGruppoDownloadBinding?=null

    private lateinit var viewModel: GruppoDownloadViewModel


    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         var gruppoIdSpecifico = arguments?.getString("gruppoShopOnline")
        gruppoIdSpecifico=gruppoIdSpecifico.toString()
        Log.e("gruppoDownl",gruppoIdSpecifico)

        _binding = FragmentGruppoDownloadBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, GruppoDownloadViewModelFactory(requireActivity().application, gruppoIdSpecifico)).get(GruppoDownloadViewModel::class.java)
        val ContenitoreDecks = binding.gruppoDownload


        val downloadButton = binding.downloadButton
        downloadButton.setOnClickListener {
            val selectedDecks = viewModel.getSelectedDecks()

            // Ora puoi passare selectedDecks e currentGruppo al tuo database locale
            // Esegui le operazioni necessarie qui per salvare i dati nel tuo database
            viewModel.downloadSelectedDecks(selectedDecks, gruppoIdSpecifico)
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
                checkBox.setOnClickListener{
                    if (checkBox.isChecked) {
                        Log.e("selezionato", "")
                        viewModel.addSelectedDeck(deck)
                    } else viewModel.removeSelectedDeck(deck)
                }
            }
        })

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}