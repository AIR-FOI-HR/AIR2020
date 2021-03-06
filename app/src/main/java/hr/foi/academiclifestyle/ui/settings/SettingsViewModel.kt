package hr.foi.academiclifestyle.ui.settings

import android.app.Application
import android.text.Editable
import android.util.Log
import androidx.lifecycle.*
import hr.foi.academiclifestyle.database.getDatabase
import hr.foi.academiclifestyle.network.model.UserRequest
import hr.foi.academiclifestyle.repository.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class SettingsViewModel(application: Application) : AndroidViewModel(application) {


    private val _responseType = MutableLiveData<Int>()
    val responseType :LiveData<Int> get() =_responseType

    private val _firstName = MutableLiveData<String>()
    val firstName: LiveData<String> get() = _firstName

    private val _lastName = MutableLiveData<String>()
    val lastName: LiveData<String> get() = _lastName

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username

    private val _study = MutableLiveData<Int>()
    val study: LiveData<Int> get() = _study

    private val _yearOfStudy = MutableLiveData<Int>()
    val yearOfStudy: LiveData<Int> get() = _yearOfStudy

    private val _semester = MutableLiveData<Int>()
    val semester: LiveData<Int> get() = _semester

    private val _picture = MutableLiveData<Int>()
    val picture : LiveData<Int> get() = _picture

    private var _imageFile = MutableLiveData<File>()
    val imageFile: LiveData<File> get() = _imageFile


    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val database = getDatabase(application)
    private val repository = MainRepository(database)

    val user =repository.user

    fun updateUser() {
        //send request
            coroutineScope.launch {
                //Construct image request
                try {
                    var id =user?.value?.profilePicture!!
                       // if(user.value!!.bitmapImage != null)
                    if(_imageFile.value != null) {
                        id = repository.uploadPicture(_imageFile.value!!, user?.value?.jwtToken!!)
                    }

                    if (_semester.value != 0 && _semester.value!! > 2) {
                        throw java.lang.IllegalArgumentException()
                    }

                    //construct UserRequest
                    val _semester1 = _semester
                    val userRequest: UserRequest =
                            UserRequest(
                                    _firstName.value.toString(),
                                    _lastName.value.toString(),
                                    _study.value!!.toInt(),
                                    _yearOfStudy.value!!.toInt(),
                                    _semester.value!!.toInt(),
                                    id
                            )
                     repository.updateUser(userRequest, user?.value?.jwtToken!!,user?.value?.rememberMe!!)
                    _responseType.value = 1

               } catch (ex: Exception) {
                    if (ex is SocketTimeoutException)
                        _responseType.value = 3
                    else if (ex is UnknownHostException)
                        _responseType.value = 3
                    else if (ex is HttpException) {
                        _responseType.value = 2
                    } else if (ex is java.lang.IllegalArgumentException) {
                        _responseType.value = 4
                    }
                    else
                        Log.i("CoroutineInfo error", ex.message.toString())
                }

        }
    }

    fun resetImage(){
        coroutineScope.launch {
            try{
                val userRequest: UserRequest =
                        UserRequest(
                                _firstName.value.toString(),
                                _lastName.value.toString(),
                                _study.value!!.toInt(),
                                _yearOfStudy.value!!.toInt(),
                                _semester.value!!.toInt(),
                                0
                        )
                repository.updateUser(userRequest, user?.value?.jwtToken!!,user?.value?.rememberMe!!)
            }catch (ex : java.lang.Exception){
                Log.e("Error", ex.toString())
            }
        }
    }

    //cancel request call if the view closes
    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }

    fun setFirstName(s: Editable){
        _firstName.value = s.toString()
    }

    fun setLastName(s: Editable){
        _lastName.value = s.toString()
    }

    fun setEmail(s: Editable){
        _email.value = s.toString()
    }

    fun setUserName(s: Editable){
        _username.value = s.toString()
    }

    fun setStudy(s: Int){
        try{
        _study.value = s.toString().toInt()
        } catch(ex :Exception){}
    }

    fun setYearOfStudy(s: Editable){
        try {
            _yearOfStudy.value = s.toString().toInt()
        }catch(ex: Exception){}
    }

    fun setSemester(s: Editable){
        try{
            _semester.value = s.toString().toInt()
        }catch (ex: Exception){}
    }

    fun setPicture(s : File){
        try{
        _imageFile.value = s
        }catch(ex : Exception){}
    }



    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SettingsViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

