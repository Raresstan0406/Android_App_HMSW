package eu.test.healthmonitorsmartwatch.ui.theme


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.ChatroomRepository
import data.Injection
import data.Result
import data.Room
import data.Users
import kotlinx.coroutines.launch


class ChatroomViewModel: ViewModel()  {
    private val _users = MutableLiveData<List<Users>>()
    val users: LiveData<List<Users>> get() = _users
    private val roomRepository: ChatroomRepository
    private val _rooms = MutableLiveData<List<Room>>()
    val rooms: LiveData<List<Room>> get() = _rooms
    init {
        roomRepository = ChatroomRepository(Injection.instance())
    }
    fun loadSelectedUsers(accountType: String, friendEmails: List<String>){
        if (accountType == "Doctor"){
            loadUsers(accountType = "Pacient", friendEmails)
        }
        else if (accountType == "Pacient"){
            loadUsers(accountType = "Doctor", friendEmails)
        }
    }
    fun createRoom(name: String) {
        viewModelScope.launch {
            roomRepository.createRoom(name)
        }
    }
    fun loadUsers(accountType: String, friendEmails: List<String>) {
        viewModelScope.launch {
            when (val result = roomRepository.getUsers()) {
                is Result.Success -> _users.value = result.data.filter { it.accountType == accountType && !friendEmails.contains(it.email)}
                is Error -> {
                }
                else ->{
                }
            }
        }
    }

    fun loadRooms(email: String) {
        viewModelScope.launch {
            when (val result = roomRepository.getRooms()) {
                is Result.Success -> _rooms.value = result.data.filter { it.name. contains(email) }
                is Error -> {

                }
                else ->{

                }
            }
        }
    }
}