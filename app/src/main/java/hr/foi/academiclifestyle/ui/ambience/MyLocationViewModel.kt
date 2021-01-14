package hr.foi.academiclifestyle.ui.ambience

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
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MyLocationViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val repository = MainRepository(database)
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    private val _responseType = MutableLiveData<Int>()
    val responseType: LiveData<Int> get() = _responseType
    private val _dataUpdated = MutableLiveData<Boolean>()
    val dataUpdated: LiveData<Boolean> get() = _dataUpdated

    var firstCall: Boolean = false

    var user = repository.user
    var sensorData = repository.sensorData

    fun fetchSensorData(roomId: Int) {
        coroutineScope.launch {
            try {
                _dataUpdated.value = repository.updateSensorData(1, roomId, "")
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
        _dataUpdated.value = null
    }

    //cancel request call if the view closes
    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OtherRoomsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return OtherRoomsViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}