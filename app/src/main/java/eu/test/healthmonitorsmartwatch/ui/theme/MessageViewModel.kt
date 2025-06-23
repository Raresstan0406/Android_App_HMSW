package eu.test.healthmonitorsmartwatch.ui.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import data.Injection
import data.Message
import data.MessageRepository
import data.Result
import data.User
import data.UserRepositoryFireBase
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {

    private val messageRepository: MessageRepository
    private val userRepository: UserRepositoryFireBase
    init {
        messageRepository = MessageRepository(Injection.instance())
        userRepository = UserRepositoryFireBase(
            FirebaseAuth.getInstance(),
            Injection.instance()
        )
        loadCurrentUser()
    }
    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    private val _roomName = MutableLiveData<String>()
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    private fun loadCurrentUser() {
        viewModelScope.launch {
            when (val result = userRepository.getCurrentUser()) {
                is Result.Success -> _currentUser.value = result.data as User?
                is Error -> {
                }
                else -> {}
            }
        }
    }
    fun loadMessages() {
        viewModelScope.launch {
            if (_roomName != null) {
                messageRepository.getChatMessages(_roomName.value.toString())
                    .collect { _messages.value = it }
            }
        }
    }
    fun sendMessage(text: String) {
        if (_currentUser.value != null) {
            val message = Message(
                senderFirstName = _currentUser.value!!.firstName,
                senderLastName = _currentUser.value!!.lastName,
                senderId = _currentUser.value!!.email,
                text = text
            )
            viewModelScope.launch {
                when (messageRepository.sendMessage(_roomName.value.toString(), message)) {
                    is Result.Success -> Unit
                    is Error -> {

                    }
                    else -> {}
                }
            }
        }
    }
    fun setRoomName(roomName: String) {
        _roomName.value = roomName
        loadMessages()
    }

}