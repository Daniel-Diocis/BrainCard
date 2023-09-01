package com.example.braincard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
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
import com.example.braincard.databinding.FragmentGruppoDownloadBinding
import com.example.braincard.databinding.FragmentShopBinding

class Gruppo_DownloadFragment : Fragment() {
    private var _binding: FragmentGruppoDownloadBinding?=null

    private lateinit var viewModel: GruppoDownloadViewModel
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGruppoDownloadBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(GruppoDownloadViewModel::class.java)
        val ContenitoreDecks = binding.gruppoDownload
        val gruppoIdSpecifico = "5NKoqNeN7Pis9Vrh2SCe"

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
}