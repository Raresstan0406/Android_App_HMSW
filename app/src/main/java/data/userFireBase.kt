package data

data class User(
    val firstName: String = "",
    val lastName: String = "",
    val accountType: String = "",
    val email: String = "",
    val friends: List<String> = emptyList()
)

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}