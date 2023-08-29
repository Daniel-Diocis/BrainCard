package com.example.braincard

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.braincard.data.model.Card
import com.example.braincard.databinding.FragmentFlashcardStudioBinding
import com.example.braincard.databinding.FragmentVisualizzaFlashcardOnlineBinding
import com.example.braincard.factories.FlashcardStudioViewModelFactory
import com.example.braincard.factories.VisualizzaFlashcardOnlineViewModelFactory

class VisualizzaFlashcardOnline : Fragment() {

    lateinit var deckId : String
    lateinit var viewModel : VisualizzaFlashcardOnlineViewModel
    lateinit var binding : FragmentVisualizzaFlashcardOnlineBinding
    lateinit var flashcardPagerAdapter: FlashcardPagerAdapter2
    var lastClickedPosition = 0
     var size : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        deckId = "pippo"
        viewModel = ViewModelProvider(this, VisualizzaFlashcardOnlineViewModelFactory(requireActivity().application, deckId.toString())).get(
            VisualizzaFlashcardOnlineViewModel::class.java)



        binding = FragmentVisualizzaFlashcardOnlineBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.AllCards.observe(viewLifecycleOwner){cards->
            if(cards.isNullOrEmpty()) viewModel.createAllCards()
            Log.e("CARDS", cards.toString())
            flashcardPagerAdapter = FlashcardPagerAdapter2(cards)
            binding.viewPager.adapter = flashcardPagerAdapter
            size = cards.size

        }
        binding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })
    }
    


}
class FlashcardPagerAdapter2(private val flashcards: MutableList<Card>) :
    RecyclerView.Adapter<FlashcardViewHolder2>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardViewHolder2 {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.flashcard_on, parent, false) //<-- IMPORTANTE
        return FlashcardViewHolder2(itemView)
    }

    override fun onBindViewHolder(holder: FlashcardViewHolder2, position: Int) {
        val card = flashcards[position]
        holder.bind(card)
        holder.itemView.setOnClickListener{
            holder.toggleCardVisibility()
        }
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



class FlashcardViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val domandaTextView: TextView = itemView.findViewById(R.id.editDomanda2)
    private val rispostaTextView: TextView = itemView.findViewById(R.id.editRisposta2)
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

    }

}