package hr.foi.academiclifestyle.network.model

import com.squareup.moshi.Json

data class Sensor (
    @field:Json(name = "@id")
    val id: Int = 0,
    @field:Json(name = "@name")
    val name: String? = "",
    @field:Json(name = "@sensor_type")
    val sensorType: Int? = 0,
    @field:Json(name = "@room")
    val room: Int? = 0
)