package hr.foi.academiclifestyle.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.foi.academiclifestyle.database.model.Sensor

@Dao
interface SensorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSensor(sensor: Sensor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSensors(sensors: MutableList<Sensor>)

    @Query("DELETE FROM sensor WHERE tab = :tab")
    fun clearSensor(tab: Int)

    @Query("DELETE FROM sensor")
    fun clearSensors()

    @Query("SELECT * FROM sensor")
    fun getSensors(): LiveData<List<Sensor>>?
}