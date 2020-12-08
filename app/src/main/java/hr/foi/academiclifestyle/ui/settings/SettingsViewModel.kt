package hr.foi.academiclifestyle.ui.settings

import android.app.Application
import android.graphics.Picture
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.foi.academiclifestyle.database.getDatabase
import hr.foi.academiclifestyle.network.model.UserRequest
import hr.foi.academiclifestyle.repository.MainRepository
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
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )
    private val database = getDatabase(application)
    private val repository = MainRepository(database)


    fun updateUser() {
        Log.i("CoroutineInfo", coroutineScope.toString())
        if (_firstName.value == null || _firstName.value == "" ||
                _lastName.value == null || _lastName.value == "" ||
                _study.value == null || _study.value == "" ||
                _yearOfStudy.value == null || _yearOfStudy.value == 0

        ) {
            _responseType.value = 1
        } else {
            //construct UserRequest
            val userRequest: UserRequest =
                    UserRequest(
                            _firstName.value.toString(),
                            _lastName.value.toString(),
                            _password.value.toString(),
                            _username.value.toString(),
                            _study.value.toString(),
                            _yearOfStudy.value!!.toInt()
                           // _picture.value.toString()
                    )

            //send request
            coroutineScope.launch {
                try {
                    repository.updateUser(userRequest)
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
}