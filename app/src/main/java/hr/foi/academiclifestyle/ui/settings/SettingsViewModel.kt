package hr.foi.academiclifestyle.ui.settings

import android.app.Application
import android.graphics.Picture
import android.text.Editable
import android.util.Log
import androidx.lifecycle.*
import hr.foi.academiclifestyle.database.getDatabase
import hr.foi.academiclifestyle.database.model.User
import hr.foi.academiclifestyle.network.model.UserRequest
import hr.foi.academiclifestyle.repository.MainRepository
import hr.foi.academiclifestyle.ui.auth.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class SettingsViewModel(application: Application) : AndroidViewModel(application) {


    private val _responseType = MutableLiveData<Int>()
    val responseType :LiveData<Int> get() =_responseType

    private val _firstName = MutableLiveData<String>()
    val firstName: LiveData<String> get() = _firstName

    private val _lastName = MutableLiveData<String>()
    val lastName: LiveData<String> get() = _lastName

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username

    private val _study = MutableLiveData<String>()
    val study: LiveData<String> get() = _study

    private val _yearOfStudy = MutableLiveData<Int>()
    val yearOfStudy: LiveData<Int> get() = _yearOfStudy

    private val _picture = MutableLiveData<Picture>()
    val picture: LiveData<Picture> get() = _picture


    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val database = getDatabase(application)
    private val repository = MainRepository(database)

    val user =repository.user

    fun updateUser() {
        if (_firstName.value == null || _firstName.value == "" ||
                _lastName.value == null || _lastName.value == "" ||
                _study.value == null || _study.value == "" ||
                _yearOfStudy.value == null || _yearOfStudy.value == 0

        ) {

        } else {
            //construct UserRequest
            val userRequest: UserRequest =
                    UserRequest(
                            _firstName.value.toString(),
                            _lastName.value.toString(),
                            _password.value.toString(),
                            _username.value.toString(),
                            //_study.value.toString(),
                            _yearOfStudy.value!!.toInt()
                           // _picture.value.toString()
                    )

            //send request
            coroutineScope.launch {
                try {
                     repository.updateUser(userRequest, user?.value?.jwtToken!!)
               } catch (ex: Exception) {
                    if (ex is SocketTimeoutException)
                        _responseType.value = 3
                    else if (ex is UnknownHostException)
                        _responseType.value = 3
                    else if (ex is HttpException)
                        _responseType.value = 2
                    else
                        Log.i("CoroutineInfo error", ex.message.toString())
                }
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

    fun setPassword(s: Editable){
        _password.value = s.toString()
    }

    fun setUserName(s: Editable){
        _username.value = s.toString()
    }

    fun setStudy(s: Editable){
        _study.value = s.toString()
    }

    fun setYearOfStudy(s: Editable){
        _yearOfStudy.value = s.toString().toInt()
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

