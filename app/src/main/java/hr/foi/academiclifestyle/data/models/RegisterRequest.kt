package hr.foi.academiclifestyle.data.models

data class RegisterRequest (
    val username: String,
    val password: String,
    val email: String
)