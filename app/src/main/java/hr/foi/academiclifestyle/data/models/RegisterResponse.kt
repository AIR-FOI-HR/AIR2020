package hr.foi.academiclifestyle.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.Field

data class RegisterResponse(
        @field:Json(name = "@jwt")
        val jwt: String ="",
        @field:Json(name = "@user")
        val user: RegisterUserData? = null,
        @field:Json(name = "@statusCode")
        val statusCode: Int = 0,
        @field:Json(name = "@message")
        val message: List<RegisterMessage>? = listOf<RegisterMessage>(),
        @field:Json(name = "@data")
        val data: List<RegisterData>? = listOf<RegisterData>())

data class RegisterUserData (
        @field:Json(name = "@")
        val id: Int = 0,
        @field:Json(name = "@username")
        val username: String = "",
        @field:Json(name = "@email")
        val email: String = "",
        @field:Json(name = "@provider")
        val provider: String = "",
        @field: Json(name = "@confirmed")
        val confirmed: Boolean,
        @field: Json(name= "@blocked")
        val blocked: String?,
        @field:Json(name = "@role")
        val role: Role? = null,
        @field:Json(name = "@created_at")
        val created_at: String = "", //timestamp?
        @field:Json(name = "@updated_at")
        val updated_at: String = "" //timestamp?
)

data class RegisterRole(
        @field: Json (name = "@id")
        val id: Int=0,
        @field: Json(name = "@name")
        val name: String = "",
        @field: Json(name = "@description")
        val description: String= "",
        @field: Json(name = "@type")
        val type: String = ""
)

data class RegisterMessage (
        @field:Json(name = "@messages")
        val messages: List<Messages>? = listOf<Messages>()
)

data class RegisterData (
        @field:Json(name = "@messages")
        val messages: List<Messages>? = listOf<Messages>()
)

data class RegisterMessages (
        @field:Json(name = "@id")
        val id: String = "",
        @field:Json(name = "@message")
        val message: String = ""
)