package hr.foi.academiclifestyle.ui.auth

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.foi.academiclifestyle.data.models.LoginRequest
import hr.foi.academiclifestyle.data.models.User
import hr.foi.academiclifestyle.data.source.DatabaseApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class LoginViewModel : ViewModel() {

    private val _responseType = MutableLiveData<Int>()
    val responseType: LiveData<Int> get() = _responseType

    private val _loginResponse = MutableLiveData<User>()
    val responseLogin: LiveData<User> get() = _loginResponse

    private val _usernameTxt = MutableLiveData<String>()
    val usernameLogin: LiveData<String> get() = _usernameTxt

    private val _passwordTxt = MutableLiveData<String>()
    val passwordLogin: LiveData<String> get() = _passwordTxt

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    fun sendLoginData() {
        Log.i("CoroutineInfo", coroutineScope.toString())
        if (_usernameTxt.value == null || _usernameTxt.value == "" ||
            _passwordTxt.value == null || _passwordTxt.value == "") {
            _responseType.value = 1
        } else {
            //construct LoginRequest
            val loginRequest: LoginRequest = LoginRequest(_usernameTxt.value.toString(), _passwordTxt.value.toString())

            //send request
            coroutineScope.launch {
                val postLoginDeferred = DatabaseApi.retrofitService.postLogin(loginRequest)
                try {
                    val response = postLoginDeferred.await()
                    if (response.jwt != "") {
                        val user = User(response.user!!.username, response.user!!.email, true)
                        _loginResponse.value = user
                    }
                } catch (ex: Exception) {
                    if (ex is SocketTimeoutException)
                        _responseType.value = 3
                    else if (ex is UnknownHostException)
                        _responseType.value = 3
                    else if (ex is HttpException)
                        _responseType.value = 2
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
}