package hr.foi.academiclifestyle.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hr.foi.academiclifestyle.database.dao.EventDao
import hr.foi.academiclifestyle.database.dao.SensorDao
import hr.foi.academiclifestyle.database.dao.SubjectDao
import hr.foi.academiclifestyle.database.model.User
import hr.foi.academiclifestyle.database.dao.UserDao
import hr.foi.academiclifestyle.database.model.Event
import hr.foi.academiclifestyle.database.model.Sensor
import hr.foi.academiclifestyle.database.model.Subject

@Database(entities = [User::class, Event::class, Subject::class, Sensor::class], version = 13, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val eventDao: EventDao
    abstract val subjectDao: SubjectDao
    abstract val sensorDao: SensorDao
}

private lateinit var INSTANCE: LocalDatabase

fun getDatabase(context: Context): LocalDatabase {
    synchronized(LocalDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                LocalDatabase::class.java,
                "localDb").fallbackToDestructiveMigration().build()
        }
    }
    return INSTANCE
}