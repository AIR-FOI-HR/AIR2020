package hr.foi.academiclifestyle.network.model

import com.squareup.moshi.Json

data class Program (
    @field:Json(name = "@id")
    val id: Int = 0,
    @field:Json(name = "@name")
    val name: String = "",
    @field:Json(name = "@short_name")
    val shortName: String = ""
)