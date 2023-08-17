import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.braincard.data.model.Card
import com.example.braincard.data.model.Deck
import com.example.braincard.database.CardDAO

import com.example.braincard.database.DeckDAO

@Database(entities = [Card::class, Deck::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun cardDao(): CardDAO
    abstract fun deckDao(): DeckDAO
    
}