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
        @field:Json(name = "@temp")
        val temp: Float? = 0f,
        @field:Json(name = "@humid")
        val humid: Float? = 0f,
        @field:Json(name = "@press")
        val press: Float? = 0f,
        @field:Json(name = "@status")
        val status: Int? = 0
)