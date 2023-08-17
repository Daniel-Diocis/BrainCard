import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.braincard.data.model.Card
import com.example.braincard.database.CardRepository
import kotlinx.coroutines.launch

class ModCreaCardViewModel(private val repository: CardRepository) : ViewModel() {
    // LiveData per i dettagli della carta.
    val cardLiveData = MutableLiveData<Card>()
    val cardIdLiveData=MutableLiveData<String>()

    // Funzione per caricare una carta dal database dato il codice univoco
    fun loadCardByCode(cardCode: String)  {
        viewModelScope.launch {
            val card = repository.getCardById(cardCode)
            cardLiveData.postValue(card)
            cardIdLiveData.postValue(card.id)
        }
    }
    fun isCardIdInDatabase(cardId: String): Boolean {
        var bool : Boolean = false
        viewModelScope.launch { bool= repository.isCardIdInDatabase(cardId) }
        return bool
    }
}
//TODO: Creare CardRepository e Model Card