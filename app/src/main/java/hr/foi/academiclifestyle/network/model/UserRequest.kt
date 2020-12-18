package hr.foi.academiclifestyle.network.model

data  class UserRequest(
        val name: String,
        val surname: String,
        val password: String,
        val username: String,
        //val program: String,
        val year: Int
       // val picture: String
)