package hr.foi.academiclifestyle.network.model

import com.squareup.moshi.Json

data class RoomEvent (
    @field:Json(name = "@id")
    val id: Int = 0,
    @field:Json(name = "@name")
    val name: String = "",
    @field:Json(name = "@capacity")
    val capacity: Int = 0,
    @field:Json(name = "@building")
    val building: Int? = 0,
    @field:Json(name = "@sensor")
    val sensor: Int? = 0
)