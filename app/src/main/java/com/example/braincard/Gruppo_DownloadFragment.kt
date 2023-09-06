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
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
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
    private var gruppoIdSpecifico= ""
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gruppoIdSpecifico= arguments?.getString("gruppoId").toString()
        _binding = FragmentGruppoDownloadBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, GruppoDownloadViewModelFactory(requireActivity().application, gruppoIdSpecifico)).get(GruppoDownloadViewModel::class.java)

        binding.creatorName.text=arguments?.getString("utente")
        binding.shortMessage.text=arguments?.getString("info")





        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ContenitoreDecks = binding.gruppoDownload
        val downloadButton = binding.downloadButton
        downloadButton.setOnClickListener {
            val selectedDecks = viewModel.getSelectedDecks()
            viewModel.downloadSelectedDecks(selectedDecks, gruppoIdSpecifico)
        }
        viewModel.deckInRoom.observe(viewLifecycleOwner, Observer { decks->
            if (decks!=null) {
                viewModel.deckGruppo.observe(viewLifecycleOwner, Observer { deckList ->
                    // Pulisci la vista dei deck esistente, se necessario
                    ContenitoreDecks.removeAllViews()
                    // Ciclo attraverso i deck e crea le viste o elementi dell'interfaccia utente
                    for (deck in deckList) {
                        val listItemView = layoutInflater.inflate(R.layout.list_item_with_icon,null)
                        val checkBox = listItemView.findViewById<CheckBox>(R.id.checkBox)
                        val searchIcon = listItemView.findViewById<ImageButton>(R.id.searchIcon)
                        checkBox.text = deck.nome
                        for (dd in decks) {
                            if (dd.id == deck.id) {
                                checkBox.isChecked = true
                                checkBox.isEnabled = false
                            }
                            // Disabilita se il deck è presente
                        }
                        checkBox.setOnClickListener {
                            if (checkBox.isChecked) {
                                viewModel.addSelectedDeck(deck)
                            } else viewModel.removeSelectedDeck(deck)
                        }
                        searchIcon.setOnClickListener{
                            val bundle = bundleOf("deckId" to deck.id)
                            findNavController().navigate(R.id.action_gruppo_DownloadFragment2_to_visualizzaFlashcardOnline, bundle)
                        }
                        ContenitoreDecks.addView(listItemView)
                    }

                    // Aggiungi il button alla vista dei deck


                })
            }
            binding.progressBar.visibility=View.GONE
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}