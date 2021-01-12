package hr.foi.academiclifestyle.network.model

import com.squareup.moshi.Json
import hr.foi.academiclifestyle.database.model.User

data class Event (
    @field:Json(name = "@id")
    val id: Int = 0,
    @field:Json(name = "@name")
    val Name: String = "",
    @field:Json(name = "@event_type")
    val event_type: EventType? = null,
    @field:Json(name = "@subject")
    val subject: Subject? = null,
    @field:Json(name = "@room")
    val room: RoomEvent? = null,
    @field:Json(name = "@user")
    val user: User? = null,
    @field:Json(name = "@day")
    val day: String = "",
    @field:Json(name = "@start_time")
    val start_time: String = "",
    @field:Json(name = "@end_time")
    val end_time: String = ""
)