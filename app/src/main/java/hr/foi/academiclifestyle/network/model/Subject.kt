package hr.foi.academiclifestyle.network.model

import com.squareup.moshi.Json

data class Subject (
    @field:Json(name = "@id")
    val id: Int = 0,
    @field:Json(name = "@name")
    val Name: String = "",
    @field:Json(name = "@Semester")
    val semester: Int = 0,
    @field:Json(name = "@program")
    val program: Int? = 0,
    @field:Json(name = "@short_name")
    val shortName: String? = ""
)