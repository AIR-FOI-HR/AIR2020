package hr.foi.academiclifestyle.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hr.foi.academiclifestyle.database.model.User
import hr.foi.academiclifestyle.database.dao.UserDao

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract val userDao: UserDao
}

private lateinit var INSTANCE: LocalDatabase

fun getDatabase(context: Context): LocalDatabase {
    synchronized(LocalDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                LocalDatabase::class.java,
                "localDb").build()
        }
    }
    return INSTANCE
}