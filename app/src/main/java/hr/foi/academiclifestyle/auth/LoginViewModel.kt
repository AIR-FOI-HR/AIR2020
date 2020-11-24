package hr.foi.academiclifestyle.auth

import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.foi.academiclifestyle.LoginActivity
import hr.foi.academiclifestyle.data.models.LoginRequest
import hr.foi.academiclifestyle.data.models.AuthResponse
import hr.foi.academiclifestyle.data.models.User
import hr.foi.academiclifestyle.database.DatabaseApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _responseType = MutableLiveData<Int>()
    val responseType: LiveData<Int> get() = _responseType

    private val _loginResponse = MutableLiveData<User>()
    val response: LiveData<User> get() = _loginResponse

    private val _usernameTxt = MutableLiveData<String>()
    val username: LiveData<String> get() = _usernameTxt

    private val _passwordTxt = MutableLiveData<String>()
    val password: LiveData<String> get() = _passwordTxt

    fun sendLoginData() {
        if (_usernameTxt.value == null || _usernameTxt.value == "" ||
            _passwordTxt.value == null || _passwordTxt.value == "") {
            Log.i("loginCheck", "Failure")
            _responseType.value = 1
        } else {
            Log.i("loginCheck", "Success")
            //construct LoginRequest
            val loginRequest: LoginRequest = LoginRequest(_usernameTxt.value.toString(), _passwordTxt.value.toString())

            //send request
            DatabaseApi.retrofitService.postLogin(loginRequest).enqueue(object: Callback<AuthResponse> {
                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    Log.i("ApiResponse", "Failure: " + t.message)
                    _responseType.value = 2
                }

                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    if(response.code() == 200 && response.body()!!.jwt !="")
                     {
                        Log.i("ApiResponse", "Success!")
                            val user = User(response.body()!!.user!!.username, response.body()!!.user!!.email, true)
                            _loginResponse.value = user
                    }
                }
            })
        }
    }

    fun setUsername(s: Editable){
        _usernameTxt.value = s.toString()
    }

    fun setPassword(s: Editable){
        _passwordTxt.value = s.toString()
    }
}