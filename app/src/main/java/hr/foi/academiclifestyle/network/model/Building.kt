package hr.foi.academiclifestyle.network.model

import com.squareup.moshi.Json

data class Building (
    @field:Json(name = "@id")
    val id: Int = 0,
    @field:Json(name = "@name")
    val name: String = ""
)