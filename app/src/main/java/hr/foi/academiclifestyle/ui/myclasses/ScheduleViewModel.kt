package hr.foi.academiclifestyle.ui.myclasses

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import hr.foi.academiclifestyle.database.getDatabase
import hr.foi.academiclifestyle.repository.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*

class ScheduleViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = MainRepository(database)

    //events & livedata
    private val _eventsUpdated = MutableLiveData<Boolean>()
    val eventsUpdated: LiveData<Boolean> get() = _eventsUpdated
    private val _responseType = MutableLiveData<Int>()
    val responseType: LiveData<Int> get() = _responseType

    var events = repository.events
    var user = repository.user

    var firstCall: Boolean = false

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    fun getEvents(day: String, program: Int) {
        coroutineScope.launch {
            var currentDay: String = ""
            if (day == "") {
                val c = Calendar.getInstance()
                c.time = Date()
                val dayOfWeek = c[Calendar.DAY_OF_WEEK]
                currentDay = when (dayOfWeek) {
                    1 -> "sunday"
                    2 -> "monday"
                    3 -> "tuesday"
                    4 -> "wednesday"
                    5 -> "thursday"
                    6 -> "friday"
                    else -> "saturday"
                }
            } else
                currentDay = day
            try {
                if (program == 0)
                    _eventsUpdated.value = repository.updateEvents(currentDay, user?.value?.program!!)
                else
                    _eventsUpdated.value = repository.updateEvents(currentDay, program)
            } catch (ex: Exception) {
                if (ex is SocketTimeoutException)
                    _responseType.value = 3
                else if (ex is UnknownHostException)
                    _responseType.value = 3
                else if (ex is HttpException)
                    _responseType.value = 2
                else if (ex is ConnectException)
                    _responseType.value = 3
                else
                    _responseType.value = 4
                Log.i("CoroutineInfo", ex.toString())
            }
        }
    }

    //reset the events after they have been called
    fun resetEvents() {
        _eventsUpdated.value = null
    }

    //cancel request call if the view closes
    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ScheduleViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}