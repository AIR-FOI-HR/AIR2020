package hr.foi.academiclifestyle.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import hr.foi.academiclifestyle.database.LocalDatabase
import hr.foi.academiclifestyle.database.model.User
import hr.foi.academiclifestyle.network.NetworkApi
import hr.foi.academiclifestyle.network.model.LoginRequest
import hr.foi.academiclifestyle.network.model.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository (private val database: LocalDatabase) {

    //declare all raw data to be used by the app here
    val user: LiveData<User>? = database.userDao.getUser()

    //declare all functions related to handling data here
    suspend fun loginUser (loginRequest: LoginRequest, rememberUser: Boolean) {
        withContext(Dispatchers.IO) {
            val response = NetworkApi.networkService.postLogin(loginRequest).await()
            var jwtToken : String = ""
            if (rememberUser)
                jwtToken = response.jwt
            if (response.jwt != "") {
                val user = User (
                    response.user!!.id,
                    response.user!!.name,
                    response.user!!.surname,
                    response.user!!.email,
                    response.user!!.year,
                    response.user!!.program,
                    "",
                    jwtToken
                )
                database.userDao.clear()
                database.userDao.insert(user)
            }
        }
    }

    suspend fun registerUser (registerRequest: RegisterRequest) : Boolean {
        return withContext(Dispatchers.IO) {
            val response = NetworkApi.networkService.postRegister(registerRequest).await()
            response.jwt != ""
        }
    }

    suspend fun checkUserToken (token: String) : Boolean {
        return withContext(Dispatchers.IO) {
            val response = NetworkApi.networkService.getUserByToken("Bearer $token").await()

            //either update user or clear
            if (response.id > 0) {
                val userRes = User (
                        response.id,
                        response.name,
                        response.surname,
                        response.email,
                        response.year,
                        response.program,
                        "",
                        token
                )
                database.userDao.clear()
                //comment out the next line and return false to simulate token expiration
                database.userDao.insert(userRes)
                true
            } else {
                database.userDao.clear()
                false
            }
        }
    }

    suspend fun clearUser () {
        withContext(Dispatchers.IO) {
            database.userDao.clear()
        }
    }
}