package hr.foi.academiclifestyle.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.foi.academiclifestyle.database.model.Event

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvents(events: List<Event>)

    @Query("DELETE FROM event")
    fun clearEvents()

    @Query("SELECT * FROM event ORDER BY start_time DESC")
    fun getEvents(): LiveData<List<Event>>?
}