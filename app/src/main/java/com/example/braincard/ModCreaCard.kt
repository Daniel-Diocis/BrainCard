package com.example.braincard

import AppDatabase
import ModCreaCardViewModel
import android.animation.ObjectAnimator
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.room.Database
import androidx.room.Room
import com.example.braincard.data.model.Card
import com.example.braincard.database.CardDAO
import com.example.braincard.database.CardRepository
import com.example.braincard.database.DeckDAO
import com.example.braincard.database.DeckRepository
import com.example.braincard.databinding.FragmentModCreaCardBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class Modcreacard : Fragment() {

    private lateinit var viewModel: ModCreaCardViewModel
    private lateinit var binding: FragmentModCreaCardBinding
    var dom : String = ""
    var risp : String = ""
    lateinit var cardRepository : CardRepository
    lateinit var deckRepository : DeckRepository
    var bool : Boolean = false
    lateinit var cardCode : String
    private val fragmentScope = CoroutineScope(Dispatchers.Main)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "app-database").build()
        val cardDao = db.cardDao()
        val deckDAO = db.deckDao()
        cardRepository=CardRepository(cardDao)
        deckRepository= DeckRepository(deckDAO)

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
        cardCode = generateRandomString(20) // TODO: NON VA BENE

// Controlla se la carta è già presente nel database
        if (viewModel.isCardIdInDatabase(cardCode)) {
            bool=true
            // Se la carta è presente, carica i dettagli nel layout XML
            viewModel.loadCardByCode(cardCode)

            // Osserva il LiveData per i dettagli della carta e aggiorna l'interfaccia utente
            viewModel.cardLiveData.observe(viewLifecycleOwner, Observer { card: Card ->
                // Aggiorna l'interfaccia utente con i dati della carta
                binding.editDomanda.setText(card.domanda)
                binding.editRisposta.setText(card.risposta)
                binding.numbersTextView.setText(deckRepository.findCardPositionInDeck(deckRepository.findDeckIdForCard(deckRepository.getAllDeck(),cardCode).toString(), cardCode))
            })
        } else {
            // La carta non è presente nel database, fai qualcos'altro o mostra un messaggio
        }

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




        fragmentScope.launch {
            if (bool) existingFlashcard =
                cardRepository.getCardById(viewModel.cardIdLiveData.toString())


            if (existingFlashcard != null) {
                // Aggiorna la domanda e la risposta della flashcard esistente
                existingFlashcard.domanda = domanda
                existingFlashcard.risposta = risposta
                cardRepository.updateCard(existingFlashcard)
            } else {

                val newFlashcard = Card(cardCode, domanda, risposta, false)
                cardRepository.insertCard(newFlashcard)
                //TODO : Passare al fragment in cui si sceglie a che deck assegnare la card

            }
        }
    }
    fun generateRandomString(length: Int): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }
    override fun onDestroyView() {
        fragmentScope.cancel()
        super.onDestroyView()
    }


}
