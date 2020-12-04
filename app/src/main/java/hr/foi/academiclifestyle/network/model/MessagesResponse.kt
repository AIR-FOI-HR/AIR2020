package hr.foi.academiclifestyle.network.model

import com.squareup.moshi.Json

data class MessagesResponse (
        @field:Json(name = "@id")
        val id: String = "",
        @field:Json(name = "@message")
        val message: String = ""
)