package hr.foi.academiclifestyle.network.model

data class LoginRequest(
    val identifier: String,
    val password: String
)