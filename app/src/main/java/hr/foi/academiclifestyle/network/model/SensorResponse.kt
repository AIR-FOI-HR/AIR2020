package hr.foi.academiclifestyle.network.model

import com.squareup.moshi.Json

data class SensorResponse (
        @field:Json(name = "@id")
        val id: Int = 0,
        @field:Json(name = "@sensor")
        val sensor: Sensor? = null,
        @field:Json(name = "@results")
        val Results: SensorResults? = null,
        @field:Json(name = "@publishedAt")
        val published_at: String? = ""
)