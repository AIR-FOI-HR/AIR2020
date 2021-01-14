package hr.foi.academiclifestyle.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "sensor")
data class Sensor (
        @PrimaryKey(autoGenerate = true)
        var sensorId: Int = 0,

        val x: Float? = null,
        val y: Float? = null,
        val z: Float? = null,

        val temp: Float? = null,
        val humid: Float? = null,
        val press: Float? = null,

        val tab: Int? = 0
)