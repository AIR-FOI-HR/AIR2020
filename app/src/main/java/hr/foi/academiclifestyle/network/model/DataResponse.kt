package hr.foi.academiclifestyle.network.model

import com.squareup.moshi.Json

data class DataResponse (
        @field:Json(name = "@messages")
        val messages: List<MessagesResponse>? = listOf<MessagesResponse>()
)