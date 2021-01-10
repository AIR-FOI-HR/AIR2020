package hr.foi.academiclifestyle.ui.ambience

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hr.foi.academiclifestyle.database.getDatabase
import hr.foi.academiclifestyle.repository.MainRepository

class AmbienceViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val repository = MainRepository(database)

    var user = repository.user

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AmbienceViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AmbienceViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}