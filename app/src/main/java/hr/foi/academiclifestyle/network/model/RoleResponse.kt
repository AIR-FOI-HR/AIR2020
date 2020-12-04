package hr.foi.academiclifestyle.network.model

import com.squareup.moshi.Json

data class RoleResponse (
        @field:Json(name = "@id")
        val id: Long = 0,
        @field:Json(name = "@name")
        val name: String = "",
        @field:Json(name = "@description")
        val description: String = "",
        @field:Json(name = "@type")
        val type: String = ""
)