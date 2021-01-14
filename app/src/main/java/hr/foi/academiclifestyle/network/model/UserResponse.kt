package hr.foi.academiclifestyle.network.model

import android.graphics.Bitmap
import com.squareup.moshi.Json

data class UserResponse (
    @field:Json(name = "@id")
    val id: Long = 0,
    @field:Json(name = "@username")
    val username: String = "",
    @field:Json(name = "@email")
    val email: String = "",
    @field:Json(name = "@provider")
    val provider: String = "",
    @field:Json(name = "@confirmed")
    val confirmed: Boolean = false,
    @field:Json(name = "@blocked")
    val blocked: String? = "",
    @field:Json(name = "@role")
    val role: RoleResponse? = null,
    @field:Json(name = "@created_at")
    val created_at: String = "", //timestamp?
    @field:Json(name = "@updated_at")
    val updated_at: String = "", //timestamp?
    @field:Json(name = "@name")
    val name: String? = "",
    @field:Json(name = "@surname")
    val surname: String? = "",
    @field:Json(name = "@logToken")
    val logToken: String? = "",
    @field:Json(name = "@year")
    val year: String? = "",
    @field:Json(name = "@program")
    val program: Program? = null,
    @field:Json(name = "@profile_picture")
    val profile_picture: ImageResponse? =  null,
    @field:Json(name = "@semester")
    val semester: Int = 0
)