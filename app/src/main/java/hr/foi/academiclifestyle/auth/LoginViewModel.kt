package hr.foi.academiclifestyle.auth

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.foi.academiclifestyle.data.models.LoginRequest
import hr.foi.academiclifestyle.data.models.LoginResponse
import hr.foi.academiclifestyle.data.models.User
import hr.foi.academiclifestyle.database.DatabaseApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _loginResponse = MutableLiveData<User>()
    val response: LiveData<User> get() = _loginResponse

    private val _usernameTxt = MutableLiveData<String>()
    val username: LiveData<String> get() = _usernameTxt

    private val _passwordTxt = MutableLiveData<String>()
    val password: LiveData<String> get() = _passwordTxt

    fun sendLoginData() {
        if (_usernameTxt.value == null || _usernameTxt.value == "" ||
            _passwordTxt.value == null || _passwordTxt.value == "") {
            //TODO add checks for null fields and print a message
            Log.i("loginCheck", "Failure")
        } else {
            Log.i("loginCheck", "Success")
            //construct LoginRequest
            val loginRequest: LoginRequest = LoginRequest(_usernameTxt.value.toString(), _passwordTxt.value.toString())

            //send request
            DatabaseApi.retrofitService.postLogin(loginRequest).enqueue(object: Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.i("ApiResponse", "Failure: " + t.message)
                    //TODO print msg if server returns an error
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
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