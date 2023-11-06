import android.util.Log
import com.example.braincard.data.model.LoggedInUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.Result
import java.io.IOException

class LoginDataSource {

    private val auth = FirebaseAuth.getInstance()


    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        return withContext(Dispatchers.IO) {
            try {

                val authResult = auth.signInWithEmailAndPassword(username, password).await()

                if (authResult.user != null) {

                    val user = LoggedInUser(authResult.user!!.uid.toString(), authResult.user!!.displayName.toString())

                    if (user != null) {
                        Result.success(user)
                    } else {
                        kotlin.Result.failure(IOException("Errore nel recupero dei dati dell'utente"))
                    }
                } else {
                    Result.failure(IOException("L'utente non è stato autenticato correttamente"))
                }
            } catch (e: Exception) {
                Result.failure(IOException("Errore nell'autenticazione", e))
            }
        }
    }

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            auth.signOut()
        }
    }
}
