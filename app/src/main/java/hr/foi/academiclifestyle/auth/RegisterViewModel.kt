package hr.foi.academiclifestyle.auth


import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.foi.academiclifestyle.data.models.AuthResponse
import hr.foi.academiclifestyle.data.models.RegisterRequest
import hr.foi.academiclifestyle.database.DatabaseApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterViewModel:ViewModel() {

    private val _responseType = MutableLiveData<Int>()
    val responseType: LiveData<Int> get() = _responseType

    private val _registerValid = MutableLiveData<Boolean>()
    val response: LiveData<Boolean> get() = _registerValid

    private val _fullNameTxt = MutableLiveData<String>()
    val fulname: LiveData<String> get() = _fullNameTxt

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

    fun sendRegisterData(){
        if (_fullNameTxt.value == null || _fullNameTxt.value == "" || _username.value == null || _username.value == ""
                || _email.value == null || _email.value == "" || _password.value == null || _password.value == "") {
            Log.i("registerCheck", "Failure")
            _responseType.value = 1
        }
        else if (!isValidEmail(_email.value.toString())) {
            _responseType.value = 2
        }
        else{

            Log.i("registerCheck", "Success")
            //construct LoginRequest
            val registerRequest: RegisterRequest = RegisterRequest(_username.value.toString(), _password.value.toString(), _email.value.toString())

            //send request
            DatabaseApi.retrofitService.postRegister(registerRequest).enqueue(object: Callback<AuthResponse> {
                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    Log.i("ApiResponse", "Failure: " + t.message)
                    _responseType.value = 3
                }

                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    if(response.code() == 200 && response.body()!!.jwt !="")
                    {
                        Log.i("ApiResponse", "Success!")
                        _registerValid.value = true
                        _responseType.value = 4
                    }
                    else{
                        _responseType.value = 5
                    }
                }
            })
        }
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

}