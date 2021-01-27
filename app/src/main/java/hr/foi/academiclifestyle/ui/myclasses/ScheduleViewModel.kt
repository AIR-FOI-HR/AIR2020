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
import org.threeten.bp.LocalDate
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

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

    fun getEvents(currentDate: LocalDate?, program: Int) {
        coroutineScope.launch {
            var parsedDate = currentDate
            if (parsedDate == null) {
                parsedDate = LocalDate.now()
            }

            try {
                if (program == 0)
                    _eventsUpdated.value = repository.updateEvents(parsedDate!!, user?.value?.program!!, user?.value?.semester!!, user?.value?.userId!!, user?.value?.year!!)
                else
                    _eventsUpdated.value = repository.updateEvents(parsedDate!!, program, user?.value?.semester!!, user?.value?.userId!!, user?.value?.year!!)
            } catch (ex: Exception) {
                when (ex) {
                    is SocketTimeoutException -> _responseType.value = 3
                    is UnknownHostException -> _responseType.value = 3
                    is HttpException -> _responseType.value = 2
                    is ConnectException -> _responseType.value = 3
                    else -> _responseType.value = 4
                }
                Log.i("CoroutineInfoSchedule", ex.toString())
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