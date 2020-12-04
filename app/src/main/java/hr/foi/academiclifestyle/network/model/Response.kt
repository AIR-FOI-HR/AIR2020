package hr.foi.academiclifestyle.network.model

import com.squareup.moshi.Json

data class Response (
        @field:Json(name = "@jwt")
        val jwt: String = "",
        @field:Json(name = "@user")
        val user: UserResponse? = null,
        @field:Json(name = "@statusCode")
        val statusCode: Int = 0,
        @field:Json(name = "@message")
        val message: List<MessageResponse>? = listOf<MessageResponse>(),
        @field:Json(name = "@data")
        val data: List<DataResponse>? = listOf<DataResponse>()
)