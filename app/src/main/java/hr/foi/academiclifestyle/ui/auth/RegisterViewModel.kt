package hr.foi.academiclifestyle.ui.auth


import android.app.Application
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.*
import hr.foi.academiclifestyle.database.getDatabase
import hr.foi.academiclifestyle.network.model.RegisterRequest
import hr.foi.academiclifestyle.network.NetworkApi
import hr.foi.academiclifestyle.repository.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val _responseType = MutableLiveData<Int>()
    val responseType: LiveData<Int> get() = _responseType

    private val _registerValid = MutableLiveData<Boolean>()
    val responseRegister: LiveData<Boolean> get() = _registerValid

    private val _fullNameTxt = MutableLiveData<String>()
    val fulname: LiveData<String> get() = _fullNameTxt

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )
    private val database = getDatabase(application)
    private val repository = MainRepository(database)

    fun sendRegisterData(){
        if (_fullNameTxt.value == null || _fullNameTxt.value == "" || _username.value == null || _username.value == ""
                || _email.value == null || _email.value == "" || _password.value == null || _password.value == "") {
            _responseType.value = 1
        }
        else if (!isValidEmail(_email.value.toString())) {
            _responseType.value = 2
        }
        else{
            //construct LoginRequest
            val registerRequest: RegisterRequest =
                RegisterRequest(
                    _username.value.toString(),
                    _password.value.toString(),
                    _email.value.toString()
                )

            //send request
            coroutineScope.launch {
                try {
                    val valid = repository.registerUser(registerRequest)
                    if (valid) {
                        _registerValid.value = true
                        _responseType.value = 4
                    }
                } catch (ex: Exception) {
                    if (ex is SocketTimeoutException)
                        _responseType.value = 3
                    else if (ex is UnknownHostException)
                        _responseType.value = 3
                    else if (ex is HttpException)
                        _responseType.value = 5
                    else
                        Log.i("error", ex.message.toString())
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
            _registerValid.value = null
    }

    fun resetFields() {
        _fullNameTxt.value = ""
        _username.value = ""
        _email.value = ""
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun setField(s: Editable, type: Int){
        if (type == 1)
            _fullNameTxt.value = s.toString()
        else if (type == 2)
            _username.value = s.toString()
        else if (type == 3)
            _email.value = s.toString()
        else if (type == 4)
            _password.value = s.toString()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}