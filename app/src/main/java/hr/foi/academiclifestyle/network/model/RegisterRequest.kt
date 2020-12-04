package hr.foi.academiclifestyle.network.model

data class RegisterRequest (
    val username: String,
    val password: String,
    val email: String
)