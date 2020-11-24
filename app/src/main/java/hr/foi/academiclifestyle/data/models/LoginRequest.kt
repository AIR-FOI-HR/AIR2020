package hr.foi.academiclifestyle.data.models

data class LoginRequest(
    val identifier: String,
    val password: String
)