package data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FieldValue

class UserRepositoryFireBase(private val auth: FirebaseAuth,
                             private val firestore: FirebaseFirestore
){
    suspend fun signUp(email: String, password: String, firstName: String, lastName: String, accountType: String, friends:List<String>): Result<Boolean> =
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val user = User(firstName, lastName, accountType,  email, friends)

            try {
                saveUserToFirestore(user)
            } catch (e: Exception) {
                Result.Error(e)
            }
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
    suspend fun login(email: String, password: String): Result<Boolean> =
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
    private suspend fun saveUserToFirestore(user: User) {
        firestore.collection("users").document(user.email).set(user).await()
    }

    suspend fun getUserByEmail(email: String): User? = try {
        val data = firestore.collection("users")
            .whereEqualTo("email", email)
            .get()
            .await()

        if (!data.isEmpty) {
            data.documents.first().toObject(User::class.java)
        } else null
    } catch (e: Exception) {
        null
    }


    suspend fun addFriend(currentUserEmail: String, friendEmail: String) {
        val currentUserRef = firestore.collection("users").document(currentUserEmail)
        val friendUserRef = firestore.collection("users").document(friendEmail)
        currentUserRef.update("friends", FieldValue.arrayUnion(friendEmail)).await()
        friendUserRef.update("friends", FieldValue.arrayUnion(currentUserEmail)).await()
    }
    suspend fun getFriendEmails(currentUserEmail: String): List<String> {
        return try {
            val userSnapshot = firestore.collection("users")
                .document(currentUserEmail)
                .get()
                .await()

            userSnapshot.get("friends") as? List<String> ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    suspend fun getCurrentUser(): Result<User> = try {
        val uid = auth.currentUser?.email
        if (uid != null) {
            val userDocument = firestore.collection("users").document(uid).get().await()
            val user = userDocument.toObject(User::class.java)
            if (user != null) {
                Result.Success(user)
            } else {
                Result.Error(Exception("User data not found"))
            }
        } else {
            Result.Error(Exception("User not authenticated"))
        }
    } catch (e: Exception) {
        Result.Error(e)
    }
}