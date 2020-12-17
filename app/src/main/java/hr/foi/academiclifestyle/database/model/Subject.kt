package hr.foi.academiclifestyle.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subject")
data class Subject (
        @PrimaryKey(autoGenerate = true)
        var subjectId: Int = 0,

        val name: String? = "",

        val shortName: String? = "",

        val semester: Int? = 0,

        val program: Int? = 0,

        val percentage: String? = ""
)