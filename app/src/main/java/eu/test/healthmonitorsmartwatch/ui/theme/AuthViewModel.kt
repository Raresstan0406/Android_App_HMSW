package eu.test.healthmonitorsmartwatch.ui.theme

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import data.Injection
import data.Result
import data.User
import data.UserRepositoryFireBase
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val userRepository: UserRepositoryFireBase
    init {
        userRepository = UserRepositoryFireBase(
            FirebaseAuth.getInstance(),
            Injection.instance()
        )
    }
    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>> get() = _authResult

    fun signUp(email: String, password: String, firstName: String, lastName: String, accountType: String, friends:List<String>) {
        viewModelScope.launch {
            _authResult.value = userRepository.signUp(email, password, firstName, lastName, accountType, friends)
        }
    }
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authResult.value = userRepository.login(email, password)
        }
    }
    private val _userValuesMap = mutableStateMapOf<String, User?>()
    val userValuesMap: Map<String, User?> get() = _userValuesMap

    fun loadUserData(email: String) {
        if (_userValuesMap.containsKey(email)) return

        viewModelScope.launch {
            val user = userRepository.getUserByEmail(email)
            _userValuesMap[email] = user
        }
    }
    val userValues = mutableStateOf<User?>(null)
    fun loadUserCurrent(email: String)
    {
        viewModelScope.launch {
            val user = userRepository.getUserByEmail(email)
            userValues.value = user
        }
    }
    val currentUserFirstName = mutableStateOf("")
    val currentUserLastName = mutableStateOf("")
    val currentUserAccountType = mutableStateOf("")
    val currentUserEmail = mutableStateOf("")
    val friendEmails = MutableLiveData<List<String>>()

    fun add(email_friend: String) {
         viewModelScope.launch {
             userRepository.addFriend(currentUserEmail.value, email_friend)
             getFriends()
         }
    }
    fun getFriends(){
        viewModelScope.launch {
            val emails = userRepository.getFriendEmails(currentUserEmail.value)
            friendEmails.value = emails
        }
    }
}