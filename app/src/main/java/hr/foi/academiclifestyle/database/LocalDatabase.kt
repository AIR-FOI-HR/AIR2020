package hr.foi.academiclifestyle.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hr.foi.academiclifestyle.database.dao.EventDao
import hr.foi.academiclifestyle.database.model.User
import hr.foi.academiclifestyle.database.dao.UserDao
import hr.foi.academiclifestyle.database.model.Event

@Database(entities = [User::class, Event::class], version = 3, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val eventDao: EventDao
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