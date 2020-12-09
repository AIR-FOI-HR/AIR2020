package hr.foi.academiclifestyle.database.model

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User (
    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0L,

    val name: String? = "",

    val surname: String? = "",

    val email: String? = "",

    val year: String? = "",

    val program: Int? = 0,

    @ColumnInfo(name = "profile_picture")
    val profilePicture: String? = "",

    @ColumnInfo(name = "jwt_token")
    val jwtToken: String? = ""
)