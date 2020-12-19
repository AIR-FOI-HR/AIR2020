package hr.foi.academiclifestyle.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import hr.foi.academiclifestyle.database.getDatabase
import hr.foi.academiclifestyle.database.model.User
import hr.foi.academiclifestyle.repository.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel (application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = MainRepository(database)

    //events
    private val _valid = MutableLiveData<Boolean>()
    val valid: LiveData<Boolean> get() = _valid

    private val _userDeleted = MutableLiveData<Boolean>()
    val userDeleted: LiveData<Boolean> get() = _userDeleted

    val user = repository.user
    var tokenChecked: Boolean = false

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    //should run in the background
    fun checkToken(user: User) {
        tokenChecked = true
        coroutineScope.launch {
            try {
                val validRes = repository.checkUserToken(user.jwtToken!!)
                _valid.value = validRes
            } catch (ex: Exception) {
                //TODO add handling for no connection
                repository.clearUser()
                _valid.value = false
            }
        }
    }

    fun logoutUser() {
        coroutineScope.launch {
            repository.clearUser()
            _userDeleted.value = true
        }
    }

    //cancel request call if the view closes
    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }

    //reset the events after they have been called - not used at the moment
    fun resetEvents(type: Int) {
        if (type == 1)
            _userDeleted.value = null
        else if (type == 2)
            _valid.value = null
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}