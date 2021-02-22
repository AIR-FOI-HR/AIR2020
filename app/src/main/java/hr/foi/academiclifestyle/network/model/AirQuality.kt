package hr.foi.academiclifestyle.network.model

import com.squareup.moshi.Json

data class AirQuality (
    @field:Json(name = "@value")
    val value: Int? = 0,
    @field:Json(name = "@quality")
    val quality: String? = ""
)