package hr.foi.academiclifestyle.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import hr.foi.academiclifestyle.database.getDatabase
import hr.foi.academiclifestyle.repository.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _subjectsEnroled = MutableLiveData<Int>()
    val subjectsEnroled :LiveData<Int> get() =_subjectsEnroled

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val database = getDatabase(application)
    private val repository = MainRepository(database)

    var user = repository.user

    fun fetchEnrolledSubjectCount(){
        coroutineScope.launch {
            try {
                _subjectsEnroled.value = repository.fetchSubjectsBySemesterAndProgram(user?.value?.program!!, user?.value?.semester!!)
            }catch (ex: Exception){
                Log.e("Error",ex.toString())
            }
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