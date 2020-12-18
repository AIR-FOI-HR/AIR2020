package hr.foi.academiclifestyle.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.foi.academiclifestyle.database.model.Subject

@Dao
interface SubjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSubjects(events: List<Subject>)

    @Query("DELETE FROM subject")
    fun clearSubjects()

    @Query("SELECT * FROM subject ORDER BY name ASC")
    fun getSubjects(): LiveData<List<Subject>>?
}