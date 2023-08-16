package com.example.braincard

import ModCreaCardViewModel
import android.animation.ObjectAnimator
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.braincard.databinding.FragmentModCreaCardBinding

class Modcreacard : Fragment() {

    private lateinit var viewModel: ModCreaCardViewModel
    private lateinit var binding: FragmentModCreaCardBinding
    var dom : String = ""
    var risp : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentModCreaCardBinding.inflate(inflater, container, false)
        binding.giraCard.setOnClickListener {
            toggleFlashcardVisibility()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ModCreaCardViewModel::class.java)

        // Carica la carta con un codice univoco
        val cardCode = "CODICE_UNIVOCO" // Sostituisci con il tuo codice
        // TODO : implementare funzione che definisce codice
        viewModel.loadCardByCode(cardCode)

        // Osserva il LiveData per i dettagli della carta e aggiorna l'interfaccia utente
        viewModel.cardLiveData.observe(viewLifecycleOwner, Observer { card ->
            // Aggiorna l'interfaccia utente con i dati della carta
            binding.editDomanda.setText(card.domanda)
            binding.editRisposta.setText(card.risposta)
            binding.numbersTextView.setText(card.numero)
        })
    }
    private fun toggleFlashcardVisibility() {

        if (binding.flashcardBack.visibility == View.GONE) dom=binding.editDomanda.text.toString()
        else risp=binding.editRisposta.text.toString()
        val rotation = if (binding.flashcardBack.visibility == View.VISIBLE) 0f else 180f
        val anim = ObjectAnimator.ofFloat(binding.flashcard, "rotationY", rotation)
        anim.duration = 300
        anim.start()

        binding.flashcard.visibility = if (rotation == 0f) View.VISIBLE else View.GONE
        binding.flashcardBack.visibility = if (rotation == 180f) View.VISIBLE else View.GONE
        if(dom != "") binding.editDomanda.setText(dom)
        if(risp != "") binding.editRisposta.setText(risp)

    }
    fun onSalvaButtonClick() {
        lateinit var existingFlashcard : Card
        val domanda = binding.editDomanda.text.toString()
        val risposta = binding.editRisposta.text.toString()
        val codice = ""

            // TODO : Implementa funzione per cercare se il codice esiste nel db
            // Ottenere il codice dalla tua flashcard o da dove necessario

        if (codice!= null) existingFlashcard = viewModel.loadCardByCode(codice.toString())


        if (existingFlashcard != null) {
                // Aggiorna la domanda e la risposta della flashcard esistente
                existingFlashcard.domanda = domanda
                existingFlashcard.risposta = risposta
                // TODO : Esegui l'aggiornamento nel database
        } else {
                // Crea una nuova flashcard e inseriscila nel database
                // TODO : Trova numero precedente per quel codice e codice deck
                val newFlashcard = Card(codiceDeck, codice, domanda, risposta, numero)
                // TODO : Esegui l'inserimento nel database

            }
    }


}
