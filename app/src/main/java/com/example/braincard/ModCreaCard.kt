package com.example.braincard

import android.animation.ObjectAnimator

import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.braincard.data.model.Card
import com.example.braincard.data.model.Deck
import com.example.braincard.databinding.FlashcardBinding
import com.example.braincard.databinding.FragmentModCreaCardBinding
import com.example.braincard.factories.ModCreaCardViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ModCreaCard : Fragment() {

    lateinit var viewModel: ModCreaCardViewModel
    private lateinit var binding: FragmentModCreaCardBinding
    private lateinit var flashcardPagerAdapter: FlashcardPagerAdapter
    private var currentCardId: String = ""
    lateinit var deckProvaID: String

    var dom: String = ""
    var risp: String = ""
    var bool: Boolean = false
    lateinit var cardCode: String
    private var lastClickedPosition: Int? = null
    private val fragmentScope = CoroutineScope(Dispatchers.Main)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentModCreaCardBinding.inflate(inflater, container, false)
        deckProvaID = arguments?.getString("deckId").toString()
        viewModel =
            ViewModelProvider(this, ModCreaCardViewModelFactory(requireActivity().application))
                .get(ModCreaCardViewModel::class.java)


        viewModel.getAllDeckCard(deckProvaID)
        binding.giraCard.setOnClickListener {
                lastClickedPosition?.let { position ->
                    // Trova il RecyclerView
                    val recyclerView = binding.viewPager.getChildAt(0) as? RecyclerView
                    recyclerView?.let {
                        // Trova il ViewHolder per la posizione corrente
                        val viewHolder = it.findViewHolderForAdapterPosition(position) as? FlashcardViewHolder
                        viewHolder?.toggleCardVisibility()


                }
            }
            }

        binding.bottoneSalva.setOnClickListener {
            onSalvaButtonClick()

        }
        binding.bottoneElimina.setOnClickListener{
            onEliminaButtonClick()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        flashcardPagerAdapter = FlashcardPagerAdapter(mutableListOf())
        binding.viewPager.adapter = flashcardPagerAdapter
        // Carica la carta con un codice univoco
        cardCode = generateRandomString(20)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.AllDeckCard.observe(viewLifecycleOwner, Observer { cardList ->
                Log.e("OBSERVER", "DENTRO " + cardList.toString())
                if (cardList.size==0){flashcardPagerAdapter = FlashcardPagerAdapter(cardList)
                binding.viewPager.adapter = flashcardPagerAdapter}
                Log.e("IF", "DENTRO N.0 : " + flashcardPagerAdapter.getItemCount())
                // Aggiorna la lista delle carte nell'adapter esistente
                flashcardPagerAdapter.updateCardList(cardList)
                // Aggiorna la vista del ViewPager
                flashcardPagerAdapter.notifyDataSetChanged()
                flashcardPagerAdapter.addFlashcard(Card(cardCode,"","",false,deckProvaID))
                binding.viewPager.setCurrentItem(flashcardPagerAdapter.getItemCount() - 1, true)

                    Log.e(
                        "IF",
                        "DENTRO: " + flashcardPagerAdapter.getItemCount() + " :::: " + cardList.toString()
                    )

                viewModel.changePercentualeByDeckID(deckProvaID, cardList)
            })
        }

        binding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottoneElimina.visibility = View.VISIBLE
                Log.e("POSIZIONE", flashcardPagerAdapter.itemCount.toString() + " :::::: "+ position.toString())
                if ( flashcardPagerAdapter.itemCount==0 || (position + 1)==flashcardPagerAdapter.itemCount ) binding.bottoneElimina.visibility = View.GONE

                lastClickedPosition = position
                if (position != flashcardPagerAdapter.itemCount) {
                    currentCardId =
                        flashcardPagerAdapter.getFlashcardAtPosition(position).id
                    val currentFlashcard =
                        flashcardPagerAdapter.getFlashcardAtPosition(position)
                    dom = currentFlashcard.domanda
                    risp = currentFlashcard.risposta
                } else {
                    currentCardId = cardCode}
                binding.numbersTextView.text = (position+1).toString()

            }
        })
    }



    fun onSalvaButtonClick() {
        lateinit var createdFlashcard: Card
        val index = binding.viewPager.currentItem
        val currentCard: Card
        val domanda: String
        val risposta: String
            currentCard = flashcardPagerAdapter.getFlashcardAtPosition(index)
            domanda = currentCard.domanda
            risposta = currentCard.risposta

        createdFlashcard = Card(currentCardId, domanda, risposta, false, deckProvaID)


        fragmentScope.launch {
            withContext(Dispatchers.IO) {

                if (viewModel.isCardIdInDatabase(currentCardId)) {
                    val existingFlashcard =
                        viewModel.getCardById(currentCardId)
                    existingFlashcard.domanda = domanda
                    existingFlashcard.risposta = risposta
                    existingFlashcard.completata = false
                    viewModel.updateCard(existingFlashcard)

                } else {
                    viewModel.insertCard(createdFlashcard)
                }
            }
            // Aggiorna la posizione del ViewPager2 per mostrare la nuova flashcard in cima
            //viene aggiornato tramite l'observer

            cardCode = generateRandomString(20)
            viewModel.getAllDeckCard(deckProvaID)


        }

    }
    fun onEliminaButtonClick()
    {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.apply {
            setTitle("Elimina Carta")
            setMessage("Sei sicuro di voler eliminare questa carta?")
            setPositiveButton("Elimina") { _, _ ->

                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        val card = viewModel.getCardById(currentCardId)
                        viewModel.deleteCard(card)
                        viewModel.getAllDeckCard(deckProvaID)
                    }
                }
            }
            setNegativeButton("Annulla", null)
            create()
            show()
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

        class FlashcardPagerAdapter(private val flashcards: MutableList<Card>) :
            RecyclerView.Adapter<FlashcardViewHolder>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardViewHolder {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.flashcard, parent, false)
                return FlashcardViewHolder(itemView)
            }

            override fun onBindViewHolder(holder: FlashcardViewHolder, position: Int) {
                val card = flashcards[position]
                holder.bind(card)
            }

            override fun getItemCount(): Int = flashcards.size
            fun addFlashcard(card: Card) {
                flashcards.add(itemCount, card)
                notifyItemInserted(itemCount)
            }

            fun getFlashcardAtPosition(position: Int): Card {
                return flashcards[position]
            }
            fun updateCardList(newCards: List<Card>) {
                flashcards.clear()
                flashcards.addAll(newCards)
                notifyDataSetChanged()
            }
        }


        class FlashcardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val domandaTextView: EditText = itemView.findViewById(R.id.editDomanda2)
            private val rispostaTextView: EditText = itemView.findViewById(R.id.editRisposta2)
            private val flashcard : CardView = itemView.findViewById(R.id.flashcard2)
            private val flashcardBack : CardView = itemView.findViewById(R.id.flashcardBack2)


            private var isBackVisible = false

            fun toggleCardVisibility() {
                Log.e("DENTRO TOGGLE", isBackVisible.toString())

                val rotation = if (isBackVisible) 0f else 180f
                val rotation2 = if (isBackVisible) 180f else 0f

                val anim = ObjectAnimator.ofFloat(flashcard, "rotationY", rotation)
                val anim2 = ObjectAnimator.ofFloat(flashcardBack, "rotationY", rotation2)


                anim.duration = 300
                anim2.duration = 300
                anim.start()
                anim2.start()
                val handler = Handler()
                handler.postDelayed({
                    flashcard.visibility = if (rotation == 0f) View.VISIBLE else View.GONE
                    flashcardBack.visibility = if (rotation == 180f) View.VISIBLE else View.GONE
                }, 320) // Ritardo di 300ms
                isBackVisible = !isBackVisible
            }

            fun bind(card: Card) {
                domandaTextView.setText(card.domanda)
                rispostaTextView.setText(card.risposta)

                domandaTextView.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        // Aggiorna il valore del campo domanda nella carta
                        card.domanda = s.toString()
                    }

                    override fun afterTextChanged(s: Editable?) {}
                })

                rispostaTextView.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        // Aggiorna il valore del campo risposta nella carta
                        card.risposta = s.toString()
                    }

                    override fun afterTextChanged(s: Editable?) {}
                })
            }

        }

