package hr.foi.academiclifestyle.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import hr.foi.academiclifestyle.database.getDatabase
import hr.foi.academiclifestyle.dimens.TardinessData
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

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _subjectsEnroled = MutableLiveData<Int>()
    val subjectsEnroled :LiveData<Int> get() =_subjectsEnroled

    private val _attendance = MutableLiveData<Float>()
    val attendance : LiveData<Float> get() = _attendance

    private val _tardiness = MutableLiveData<TardinessData>()
    val tardiness : LiveData<TardinessData> get() = _tardiness

    private val _averageTardiness = MutableLiveData<TardinessData>()
    val averageTardiness: LiveData<TardinessData> get() = _averageTardiness

    private val _response = MutableLiveData<Int>()
    val response: LiveData<Int> get() = _response

    private val _responseType = MutableLiveData<Int>()
    val responseType: LiveData<Int> get() = _responseType

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val database = getDatabase(application)
    private val repository = MainRepository(database)

    var user = repository.user

    fun fetchEnrolledSubjectCount(){
        coroutineScope.launch {
            try {
                _subjectsEnroled.value = repository.getSubjectsBySemesterProgramYear(user?.value?.program!!, user?.value?.semester!!, user?.value?.year!!)
            }catch (ex: Exception) { //since there's multiple simultaneous calls, finish animation only on the first one
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
                Log.i("CoroutineInfoAttendance", ex.toString())
            }
        }
    }


    fun getAttendance(){
        coroutineScope.launch {
            try{
                _attendance.value = repository.getSemestarCompletionData(user?.value!!)
            }catch (ex: Exception){
                Log.e("Error", ex.toString())
            }
            _attendance.value = null

        }
    }

    fun getMyTardiness(){
        coroutineScope.launch {
            try{
                _tardiness.value = repository.myTardiness(user?.value!!)
            }catch (ex: Exception){
                Log.e("Error",ex.toString())
            }
            _tardiness.value = null

        }
    }

    fun averageTardiness(){
        coroutineScope.launch {
            try{
                _averageTardiness.value = repository.averageTardiness(user?.value!!)
            }catch(ex: Exception) {
                Log.e("Error", ex.toString())
            }
            _averageTardiness.value = null
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}