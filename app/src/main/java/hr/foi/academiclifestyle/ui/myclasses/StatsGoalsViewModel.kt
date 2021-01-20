package hr.foi.academiclifestyle.ui.myclasses

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import hr.foi.academiclifestyle.database.getDatabase
import hr.foi.academiclifestyle.dimens.StatsGoalsGraphData
import hr.foi.academiclifestyle.network.model.SubjectProgram
import hr.foi.academiclifestyle.repository.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class StatsGoalsViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = MainRepository(database)
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    //livedata & events
    private val _responseType = MutableLiveData<Int>()
    val responseType: LiveData<Int> get() = _responseType
    private val _subjectList = MutableLiveData<List<String>>()
    val subjectList: LiveData<List<String>> get() = _subjectList
    private val _graphData = MutableLiveData<StatsGoalsGraphData>()
    val graphData: LiveData<StatsGoalsGraphData> get() = _graphData

    var user = repository.user
    var statsFirstCall = false

    fun getGraphData(subjectName: String) {
        coroutineScope.launch {
            try {
                if (subjectName != "") {
                    _graphData.value = repository.getGraphDataBySubject(subjectName, user!!.value!!.userId)
                } else {
                    throw java.lang.IllegalArgumentException()
                }
            } catch (ex: Exception) {
                if (ex is SocketTimeoutException)
                    _responseType.value = 3
                else if (ex is UnknownHostException)
                    _responseType.value = 3
                else if (ex is HttpException)
                    _responseType.value = 2
                else if (ex is ConnectException)
                    _responseType.value = 3
                else if (ex is IllegalArgumentException)
                    _responseType.value = 5
                else
                    _responseType.value = 4
                Log.i("CoroutineInfo", ex.toString())
            }
        }
    }

    fun getSubjectsForSpinner(programId: Int, semester: Int) {
        coroutineScope.launch {
            try {
                _subjectList.value = repository.fetchSubjectNamesBySemesterAndProgram(programId, semester)
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
            if (modelClass.isAssignableFrom(StatsGoalsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ScheduleViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}