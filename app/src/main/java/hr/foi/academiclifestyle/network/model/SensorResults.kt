package hr.foi.academiclifestyle.network.model

import com.squareup.moshi.Json

data class SensorResults (
        @field:Json(name = "@uuid")
        val uuid: String? = "",
        @field:Json(name = "@x")
        val x: Float? = 0f,
        @field:Json(name = "@y")
        val y: Float? = 0f,
        @field:Json(name = "@z")
        val z: Float? = 0f,
        @field:Json(name = "@Temperature")
        val Temperature: Float? = 0f,
        @field:Json(name = "@Humidity")
        val Humidity: Float? = 0f,
        @Json(name = "Air pressure")
        val AirPressure: Float? = 0f,
        @Json(name = "Air quality")
        val AirQuality: AirQuality? = null,
        @field:Json(name = "@status")
        val status: Int? = 0
)