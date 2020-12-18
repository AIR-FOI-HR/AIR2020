package hr.foi.academiclifestyle.ui.auth

import android.app.Application
import android.text.Editable
import android.util.Log
import androidx.lifecycle.*
import hr.foi.academiclifestyle.database.getDatabase
import hr.foi.academiclifestyle.network.model.LoginRequest
import hr.foi.academiclifestyle.repository.MainRepository
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _responseType = MutableLiveData<Int>()
    val responseType: LiveData<Int> get() = _responseType

    private val _loginResponse = MutableLiveData<Boolean>()
    val responseLogin: LiveData<Boolean> get() = _loginResponse

    private val _usernameTxt = MutableLiveData<String>()
    val usernameLogin: LiveData<String> get() = _usernameTxt

    private val _passwordTxt = MutableLiveData<String>()
    val passwordLogin: LiveData<String> get() = _passwordTxt

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )
    private val database = getDatabase(application)
    private val repository = MainRepository(database)

    fun sendLoginData(rememberMeChecked : Boolean) {
        if (_usernameTxt.value == null || _usernameTxt.value == "" ||
            _passwordTxt.value == null || _passwordTxt.value == "") {
            _responseType.value = 1
        } else {
            //construct LoginRequest
            val loginRequest: LoginRequest =
                LoginRequest(
                    _usernameTxt.value.toString(),
                    _passwordTxt.value.toString()
                )

            //send request
            coroutineScope.launch {
                try {
                    repository.loginUser(loginRequest, rememberMeChecked)
                    _loginResponse.value = true
                } catch (ex: Exception) {
                    if (ex is SocketTimeoutException)
                        _responseType.value = 3
                    else if (ex is UnknownHostException)
                        _responseType.value = 3
                    else if (ex is HttpException)
                        _responseType.value = 2
                    else
                        Log.i("CoroutineInfo", ex.message.toString())
                }
            }
        }
    }

    //cancel request call if the view closes
    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }

    //reset the events after they have been called
    fun resetEvents(event: Int) {
        if (event == 1)
            _responseType.value = null
        else
            _loginResponse.value = null
    }

    fun setUsername(s: Editable){
        _usernameTxt.value = s.toString()
    }

    fun setPassword(s: Editable){
        _passwordTxt.value = s.toString()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}