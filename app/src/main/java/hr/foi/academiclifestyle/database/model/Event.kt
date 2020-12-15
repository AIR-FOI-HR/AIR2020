package hr.foi.academiclifestyle.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "event")
data class Event (
        @PrimaryKey(autoGenerate = true)
        var eventId: Int = 0,

        val day: String? = "",

        val date: Long? = 0L,

        val name: String? = "",

        @ColumnInfo(name = "start_time")
        val startTime: String? = "",

        @ColumnInfo(name = "end_time")
        val endTime: String? = "",

        val status: Int? = 0
)