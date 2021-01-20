package hr.foi.academiclifestyle.network.model

import com.squareup.moshi.Json
import hr.foi.academiclifestyle.database.model.User

data class Attendance (
    @field:Json(name = "@id")
    val id: Int = 0,
    @field:Json(name = "@logTime")
    val log_time: String = "",
    @field:Json(name = "@users_permissions_user")
    val users_permissions_user: EventUser? = null,
    @field:Json(name = "@event")
    val event: EventAttend? = null
)

data class EventAttend (
    @field:Json(name = "@id")
    val id: Int = 0,
    @field:Json(name = "@name")
    val Name: String = "",
    @field:Json(name = "@event_type")
    val event_type: Int? = 0,
    @field:Json(name = "@subject")
    val subject: Int? = 0,
    @field:Json(name = "@room")
    val room: Int? = 0,
    @field:Json(name = "@user")
    val user: Int? = 0,
    @field:Json(name = "@day")
    val day: String = "",
    @field:Json(name = "@start_time")
    val start_time: String = "",
    @field:Json(name = "@end_time")
    val end_time: String = "",
    @field:Json(name = "@maxAttendance")
    val max_attendance: Int? = 0
)

data class EventUser (
    @field:Json(name = "@id")
    val id: Int = 0,
    @field:Json(name = "@username")
    val username: String? = ""
)