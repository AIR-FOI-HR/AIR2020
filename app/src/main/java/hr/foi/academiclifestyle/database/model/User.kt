package hr.foi.academiclifestyle.database.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User (
    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0L,

    val name: String? = "",

    val surname: String? = "",

    val username: String? = "",

    val email: String? = "",

    val year: Int? = 0,

    val program: Int? = 0,

    val semester: Int? = 0,

    @ColumnInfo(name = "profile_picture")
    val profilePicture: Int? = 0,
    val imageURL : String? = "",
    //val bitmapImage: Bitmap? = null,

    @ColumnInfo(name = "jwt_token")
    val jwtToken: String? = "",

    val rememberMe: Boolean = false
)