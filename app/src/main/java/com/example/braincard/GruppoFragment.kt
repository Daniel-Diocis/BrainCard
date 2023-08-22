package com.example.braincard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class GruppoFragment : Fragment() {

    var count : Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_gruppo, container, false)
        val gruppoId = arguments?.getString("gruppoid")


        val gruppoViewModel=ViewModelProvider(this).get(GruppoViewModel::class.java)
        if (gruppoId != null) {
            gruppoViewModel.aggiornaLista(gruppoId)
        }

        val deckContainer=rootView.findViewById<LinearLayout>(R.id.gruppiContainer)
        val btn_gen =rootView.findViewById<Button>(R.id.btn)
        btn_gen.setOnClickListener{
            gruppoViewModel.creaDeck()
        }

        gruppoViewModel.AllDeck.observe(viewLifecycleOwner, Observer { decks ->
            deckContainer.removeAllViews()
            count = 0
            for (deck in decks) {
                val deckButton = Button(requireContext())
                deckButton.text = deck.nome
                deckButton.id = count
                deckButton.setOnClickListener {
                            val bundle = bundleOf("deckId" to deck.id)
                            findNavController().navigate(
                                R.id.action_gruppoFragment_to_flashcardStudio,
                                bundle
                            )
                        }
                count++
                deckContainer.addView(deckButton)
                }
        })
        // Inflate the layout for this fragment
        return rootView
    }






}