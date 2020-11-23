package hr.foi.academiclifestyle.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//Moshi classes
data class LoginResponse (
    @field:Json(name = "@jwt")
    val jwt: String = "",
    @field:Json(name = "@user")
    val user: UserRes? = null,
    @field:Json(name = "@statusCode")
    val statusCode: Int = 0,
    @field:Json(name = "@error")
    val error: String = "",
    @field:Json(name = "@message")
    val message: List<Message>? = listOf<Message>(),
    @field:Json(name = "@data")
    val data: List<Data>? = listOf<Data>()
)

data class UserRes (
    @field:Json(name = "@id")
    val id: Long = 0,
    @field:Json(name = "@username")
    val username: String = "",
    @field:Json(name = "@email")
    val email: String = "",
    @field:Json(name = "@provider")
    val provider: String = "",
    @field:Json(name = "@confirmed")
    val confirmed: Boolean = false,
    @field:Json(name = "@blocked")
    val blocked: String? = "",
    @field:Json(name = "@role")
    val role: Role? = null,
    @field:Json(name = "@created_at")
    val created_at: String = "", //timestamp?
    @field:Json(name = "@updated_at")
    val updated_at: String = "" //timestamp?
)

data class Role (
    @field:Json(name = "@id")
    val id: Long = 0,
    @field:Json(name = "@name")
    val name: String = "",
    @field:Json(name = "@description")
    val description: String = "",
    @field:Json(name = "@type")
    val type: String = ""
)

data class Message (
    @field:Json(name = "@messages")
    val messages: List<Messages>? = listOf<Messages>()
)

data class Data (
    @field:Json(name = "@messages")
    val messages: List<Messages>? = listOf<Messages>()
)

data class Messages (
    @field:Json(name = "@id")
    val id: String = "",
    @field:Json(name = "@message")
    val message: String = ""
)