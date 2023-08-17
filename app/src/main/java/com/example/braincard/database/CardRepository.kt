import android.widget.Toast
import com.example.braincard.MainActivity
import com.example.braincard.data.model.Card
import com.example.braincard.data.model.Deck
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class CardRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val cardsCollection: CollectionReference = firestore.collection("card")

    // Funzione per aggiungere una nuova carta al database
    fun addCard(card: Card) {
        cardsCollection.add(card)

    }

    // Funzione per ottenere tutte le carte dal database
    fun getAllCards(callback: (List<Card>) -> Unit) {
        cardsCollection.get()
            .addOnSuccessListener { result ->
                val cards = result.toObjects(Card::class.java)
                callback(cards)
            }
    }
    fun getCardByCode(code: String, callback: (Card?) -> Unit) {
        cardsCollection.document(code)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val card = documentSnapshot.toObject(Card::class.java)
                    callback(card)
                } else {
                    callback(null) // Nessuna carta trovata con il codice specificato
                }
            }
            .addOnFailureListener { e ->
                // Gestisci l'errore
                callback(null)
            }
    }
    fun getFlashcardsByCodiceDeck(deckId: String, callback: (List<Card>) -> Unit) {
        var deckPreso : Deck
        cardsCollection.whereEqualTo("deckId", deckId)
            .get()


            .addOnSuccessListener { querySnapshot ->
                val deck = querySnapshot.toObjects(Deck::class.java)
                callback(cards)
            }
            .addOnFailureListener { e ->
                // Gestisci l'errore
                callback(emptyList())
            }
    }

}
