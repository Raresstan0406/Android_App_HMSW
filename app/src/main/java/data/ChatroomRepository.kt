package data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ChatroomRepository(private val firestore: FirebaseFirestore) {
    suspend fun createRoom(name: String): Result<Unit> = try {
        val room = Room(name = name)
        firestore.collection("rooms").document(name).set(room).await()
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun getRooms(): Result<List<Room>> = try {
        val querySnapshot = firestore.collection("rooms").get().await()
        val rooms = querySnapshot.documents.mapNotNull { document ->
            document.toObject(Room::class.java)
        }
        Result.Success(rooms)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun getUsers(): Result<List<Users>> = try {
        val querySnapshot = firestore.collection("users").get().await()
        val users = querySnapshot.documents.mapNotNull { document ->
            document.toObject(Users::class.java)
        }
        Result.Success(users)
    } catch (e: Exception) {
        Result.Error(e)
    }
}