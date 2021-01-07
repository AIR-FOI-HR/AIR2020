package hr.foi.academiclifestyle.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import hr.foi.academiclifestyle.database.LocalDatabase
import hr.foi.academiclifestyle.database.model.Event
import hr.foi.academiclifestyle.database.model.Subject
import hr.foi.academiclifestyle.database.model.User
import hr.foi.academiclifestyle.network.NetworkApi
import hr.foi.academiclifestyle.network.model.LoginRequest
import hr.foi.academiclifestyle.network.model.RegisterRequest
import hr.foi.academiclifestyle.network.model.UserRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.net.HttpURLConnection
import java.net.URL


class MainRepository (private val database: LocalDatabase) {

    //declare all raw data to be used by the app here
    val user: LiveData<User>? = database.userDao.getUser()
    val events: LiveData<List<Event>>? = database.eventDao.getEvents()
    val subjects: LiveData<List<Subject>>? = database.subjectDao.getSubjects()

    //User
    suspend fun loginUser (loginRequest: LoginRequest, rememberUser: Boolean) {
        withContext(Dispatchers.IO) {
            val response = NetworkApi.networkService.postLogin(loginRequest).await()
            var rememberMe : Boolean = false
            if (rememberUser)
                rememberMe = true
            var imageURL: String = ""
            var imageID : Int? = 0
            var bitmapImage : Bitmap? = null
                if(response.user?.profile_picture != null) {
                    imageURL = response.user?.profile_picture.url
                    imageID =  response.user?.profile_picture.id
                    bitmapImage = getBitmapFromURL(imageURL)
                }
            if (response.jwt != "") {
                val program = response.user?.program?.id
                val user = User (
                    response.user!!.id,
                    response.user.name,
                    response.user.surname,
                    response.user.username,
                    response.user.email,
                    response.user.year,
                    program,
                    imageID,
                    imageURL,
                   // bitmapImage,
                    response.jwt,
                    rememberMe
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

    suspend fun checkUserToken (token: String, pictureID : Int?) : Boolean {
        return withContext(Dispatchers.IO) {
            val response = NetworkApi.networkService.getUserByToken("Bearer $token").await()

            //either update user or clear

            var imageURL: String = ""
            var imageID : Int? = 0
           // var bitmapImage : Bitmap? = bitmapImage
            if(response.profile_picture != null) {
                imageURL = response?.profile_picture.url
                imageID =  response?.profile_picture.id

                if(response.profile_picture?.id  != pictureID){
                  //  bitmapImage = getBitmapFromURL(imageURL)
                }
            }

            if (response.id > 0) {
                val program = response.program?.id
                val userRes = User (
                        response.id,
                        response.name,
                        response.surname,
                        response.username,
                        response.email,
                        response.year,
                        program,
                        imageID,
                        imageURL,
                     //   bitmapImage,
                        token,
                        true
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

    suspend fun updateUser(userRequest: UserRequest,token: String, rememberMe : Boolean) :Boolean{

        return withContext(Dispatchers.IO) {

            val response = NetworkApi.networkService.updateUser("Bearer $token", userRequest).await()

            var imageURL: String = ""
            var imageID : Int? = 0
            var bitmapImage: Bitmap? = null
            if(response.profile_picture != null) {
                imageURL = response?.profile_picture.url
                imageID =  response?.profile_picture.id
                bitmapImage = getBitmapFromURL(imageURL)
            }

            //val program = response?.program?.id
            val user = User (
                response!!.id,
                response.name,
                response.surname,
                response.username,
                response.email,
                response.year,
                response.program?.id,
                imageID,
                imageURL,
               // bitmapImage,
                token,
                rememberMe
            )

            Log.e("User", user.toString())
            database.userDao.clear()
            database.userDao.insert(user)

            response.logToken != ""
        }
    }

    suspend fun uploadPicture(file : File, token : String?): Int {
        return withContext(Dispatchers.IO){

            val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
            val body = MultipartBody.Part.createFormData("files", file.name, requestFile)
            val response = NetworkApi.networkService.uploadPicture("Bearer $token", body).await()

            response[0]!!.id
        }
    }

    //Events
    suspend fun updateEvents(day: String, programId: Int) : Boolean {
        return withContext(Dispatchers.IO) {
            val eventList = NetworkApi.networkService.getEventsForDayAndProgram(programId = programId, day = day).await()

            database.eventDao.clearEvents()
            if (eventList.isNotEmpty()) {
                var events: MutableList<Event> = mutableListOf()
                for (event in eventList) {
                    //TODO fetch and add status according to user presence
                    //TODO the current date and the presence of the user should be checked to determine the status of an event
                    val eventRes = Event (
                            event.id,
                            event.day,
                            0L,
                            event.Name,
                            event.start_time,
                            event.end_time,
                            0
                    )
                    events.add(eventRes)
                }
                database.eventDao.insertEvents(events)
            }
            true
        }
    }

    //Subjects
    suspend fun updateSubjects(programId: Int, semester: Int) {
        withContext(Dispatchers.IO) {
            val subjectList = NetworkApi.networkService.getSubjectsByProgramAndSemester(programId = programId, semester = semester).await()

            database.subjectDao.clearSubjects()
            if (subjectList.isNotEmpty()) {
                var subjects: MutableList<Subject> = mutableListOf()
                for (subject in subjectList) {
                    //TODO fetch and correct percentage
                    val subjectRes = Subject (
                            subject.id,
                            subject.Name,
                            subject.program?.shortName,
                            semester,
                            programId,
                            "0%"
                    )
                    subjects.add(subjectRes)
                }
                database.subjectDao.insertSubjects(subjects)
            }
        }
    }

    suspend fun getBitmapFromURL(src: String?): Bitmap {
       return  withContext(Dispatchers.IO) {
            val url = URL(src)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            val myBitmap = BitmapFactory.decodeStream(input)
            myBitmap
        }
    }
}
