package com.example.braincard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


class GruppoFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_gruppo, container, false)
        val gruppoViewModel=ViewModelProvider(this).get(GruppoViewModel::class.java)

        val deckContainer=rootView.findViewById<LinearLayout>(R.id.deckContainer1)

        gruppoViewModel.AllDeck.observe(viewLifecycleOwner, Observer { decks ->
            deckContainer.removeAllViews()
            for (deck in decks) {
                Log.e("controllo","Deck trovato")

                val deckButton = Button(requireContext())
                deckButton.text = "gino" // O qualsiasi altra proprietà del mazzo che vuoi visualizzare
                deckButton.setOnClickListener {
                    // Qui gestisci il click sul bottone del mazzo
                }
                deckContainer.addView(deckButton)
            }
        })



        // Inflate the layout for this fragment
        return rootView
    }





}