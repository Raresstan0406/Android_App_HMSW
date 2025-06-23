package data

data class Users(
    val lastName: String = "",
    val firstName: String = "",
    val accountType: String = "",
    val email: String = "",
){
    val username: String
        get() = "$firstName $lastName"
}
