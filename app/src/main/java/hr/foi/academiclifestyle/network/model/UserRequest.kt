package hr.foi.academiclifestyle.network.model

data  class UserRequest(
        val firstName: String,
        val lastName: String,
        val password: String,
        val username: String,
        val study: String,
        val yearOfStudy: Int
       // val picture: String
)