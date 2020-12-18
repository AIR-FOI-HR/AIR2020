package hr.foi.academiclifestyle.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import hr.foi.academiclifestyle.database.model.User

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Query("DELETE FROM user")
    fun clear()

    @Query("SELECT * FROM user ORDER BY userId DESC LIMIT 1")
    fun getUser(): LiveData<User>?

}