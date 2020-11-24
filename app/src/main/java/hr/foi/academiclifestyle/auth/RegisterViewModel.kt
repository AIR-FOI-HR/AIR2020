package hr.foi.academiclifestyle.auth


import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.foi.academiclifestyle.data.models.RegisterRequest
import hr.foi.academiclifestyle.data.models.RegisterResponse
import hr.foi.academiclifestyle.data.models.User
import hr.foi.academiclifestyle.database.DatabaseApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterViewModel:ViewModel() {


    private val _registerResponse = MutableLiveData<User>()
    val response: LiveData<User> get() = _registerResponse

    private val _fullNameTxt = MutableLiveData<String>()
    val fulname: LiveData<String> get() = _fullNameTxt

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

    fun sendRegisterData(){
        if(_fullNameTxt.value == null || _fullNameTxt.value == "" || _username.value == null || _username.value == ""
                || _email.value == null || _email.value == "" || _password.value == null || _password.value == ""){
            //TODO add checks for null fields and print a message
            Log.i("registerCheck", "Failure")
        }
        else{

            Log.i("registerCheck", "Success")
            //construct LoginRequest
            val registerRequest: RegisterRequest = RegisterRequest(_username.value.toString(), _password.value.toString(), _email.value.toString())

            //send request
            DatabaseApi.retrofitService.postRegister(registerRequest).enqueue(object: Callback<RegisterResponse> {
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Log.i("ApiResponse", "Failure: " + t.message)
                    //TODO print msg if server returns an error
                }

                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if(response.code() == 200 && response.body()!!.jwt !="")
                    {
                        Log.i("ApiResponse", "Success!")
                    }
                }
            })
        }
    }

    fun setUsername(s: Editable){
        _username.value = s.toString()
    }

    fun setPassword(s: Editable){
        _password.value = s.toString()
    }

}