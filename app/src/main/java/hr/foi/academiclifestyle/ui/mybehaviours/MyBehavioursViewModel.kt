package hr.foi.academiclifestyle.ui.mybehaviours

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import hr.foi.academiclifestyle.database.getDatabase
import hr.foi.academiclifestyle.dimens.TardinessData
import hr.foi.academiclifestyle.repository.MainRepository
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MyBehavioursViewModel(application: Application) : AndroidViewModel(application) {
    private val _tardiness = MutableLiveData<TardinessData>()
    val tardiness: LiveData<TardinessData> get() = _tardiness

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val database = getDatabase(application)
    private val repository = MainRepository(database)

    private val _responseType = MutableLiveData<Int>()
    val responseType: LiveData<Int> get() = _responseType

    var user = repository.user

    fun getTardiness(){
        coroutineScope.launch {
            try{
                _tardiness.value = repository.myTardiness(user?.value!!)
            }catch (ex: Exception){
                when (ex) {
                    is SocketTimeoutException -> _responseType.value = 3
                    is UnknownHostException -> _responseType.value = 3
                    is HttpException -> _responseType.value = 2
                    is ConnectException -> _responseType.value = 3
                    else -> _responseType.value = 4
                }
                Log.i("CoroutineInfoAttendance", ex.toString())
            }
            _tardiness.value = null
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MyBehavioursViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MyBehavioursViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}