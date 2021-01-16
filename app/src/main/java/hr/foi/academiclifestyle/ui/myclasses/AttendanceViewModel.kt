package hr.foi.academiclifestyle.ui.myclasses

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import hr.foi.academiclifestyle.database.getDatabase
import hr.foi.academiclifestyle.database.model.Event
import hr.foi.academiclifestyle.database.model.Subject
import hr.foi.academiclifestyle.dimens.AttendanceDetails
import hr.foi.academiclifestyle.repository.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AttendanceViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = MainRepository(database)

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    private val _responseType = MutableLiveData<Int>()
    val responseType: LiveData<Int> get() = _responseType
    private val _details = MutableLiveData<List<AttendanceDetails>>()
    val details: LiveData<List<AttendanceDetails>> get() = _details

    var user = repository.user
    var subjects = repository.subjects
    var firstCall: Boolean = false

    fun getSubjects(programId: Int, semester: Int) {
        coroutineScope.launch {
            try {
                if (programId == 0)
                    repository.updateSubjects(user?.value?.program!!, semester)
                else
                    repository.updateSubjects(programId, semester)
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

    fun getSubjectDetails(subject: Subject) {
        coroutineScope.launch {
            try {
                _details.value = repository.getSubjectDetails(subject, user?.value?.userId!!)
                Log.i("details", _details.value.toString())
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

    //cancel request call if the view closes
    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AttendanceViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AttendanceViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}