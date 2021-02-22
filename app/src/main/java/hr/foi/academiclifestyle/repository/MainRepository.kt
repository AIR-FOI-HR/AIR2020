package hr.foi.academiclifestyle.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import hr.foi.academiclifestyle.database.LocalDatabase
import hr.foi.academiclifestyle.database.model.Event
import hr.foi.academiclifestyle.database.model.Sensor
import hr.foi.academiclifestyle.database.model.Subject
import hr.foi.academiclifestyle.database.model.User
import hr.foi.academiclifestyle.dimens.AttendanceDetails
import hr.foi.academiclifestyle.dimens.EventTypeEnum
import hr.foi.academiclifestyle.dimens.StatsGoalsGraphData
import hr.foi.academiclifestyle.dimens.TardinessData
import hr.foi.academiclifestyle.network.NetworkApi
import hr.foi.academiclifestyle.network.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import java.io.File
import java.net.HttpURLConnection
import java.net.URL


class MainRepository (private val database: LocalDatabase) {

    //declare all raw data to be used by the app here
    val user: LiveData<User>? = database.userDao.getUser()
    val events: LiveData<List<Event>>? = database.eventDao.getEvents()
    val subjects: LiveData<List<Subject>>? = database.subjectDao.getSubjects()
    val sensorData: LiveData<List<Sensor>>? = database.sensorDao.getSensors()

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
                    response.user!!.semester,
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
                        response.semester,
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
                response.semester,
                imageID,
                imageURL,
               // bitmapImage,
                token,
                rememberMe
            )

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


    suspend fun getSubjectsBySemesterProgramYear(programId: Int, semester: Int, year: Int) : Int{
       return withContext(Dispatchers.IO){
           //calculate semester based on year
           var realSemester = semester
           if (semester == 1) {
               realSemester = (year*2)-1
           } else {
               realSemester = (year*2)
           }

           val response = NetworkApi.networkService.getSubjectsByProgramAndSemester(programId = programId, semester = realSemester).await()
           response.size
        }
    }

    suspend fun getSubjectNamesBySemesterProgramYear(programId: Int, semester: Int, year: Int) : List<String> {
        return withContext(Dispatchers.IO){
            //calculate semester based on year
            var realSemester = semester
            if (semester == 1) {
                realSemester = (year*2)-1
            } else {
                realSemester = (year*2)
            }

            val response = NetworkApi.networkService.getSubjectsByProgramAndSemester(programId = programId, semester = realSemester).await()
            val subjectNames: MutableList<String> = mutableListOf()
            if (response.isNotEmpty()) {
                for (subject in response) {
                    subjectNames.add(subject.Name)
                }
            }
            subjectNames
        }
    }

    //Events
    suspend fun updateEvents(date: LocalDate, programId: Int, semester: Int, userId: Long, year: Int) : Boolean {
        return withContext(Dispatchers.IO) {
            val day = date.dayOfWeek.toString().toLowerCase()

            //calculate semester based on year
            var realSemester = semester
            if (semester == 1) {
                realSemester = (year*2)-1
            } else {
                realSemester = (year*2)
            }

            val eventList = NetworkApi.networkService.getEventsForDayProgramSemester(programId = programId, day = day, semester = realSemester).await()
            val attendances = NetworkApi.networkService.getAttendanceByUserId(userId).await()

            database.eventDao.clearEvents()
            if (eventList.isNotEmpty()) {
                val events: MutableList<Event> = mutableListOf()
                for (event in eventList) {
                    var userLoggedDate = ""
                    var pressence = 2 //not present by default
                    if (attendances.isNotEmpty()) {
                        for (attendance in attendances) {

                            if (attendance.event!!.id == event.id && attendance.event.event_type == event.event_type!!.id) {
                                val time = attendance.log_time
                                var parsedDateTime = LocalDateTime.parse(time.substring(0, time.length - 1))
                                parsedDateTime = parsedDateTime.plusHours(1) //Strapi returns the wrong timezone

                                val hourStart = event.start_time.substring(0, event.start_time.length-3).toInt()
                                val minutesStart = event.start_time.substring(3, event.start_time.length).toInt()
                                val dateTimeStart = LocalDateTime.of(date.year, date.monthValue, date.dayOfMonth, hourStart, minutesStart)

                                val hourEnd = event.end_time.substring(0, event.end_time.length-3).toInt()
                                val minutesEnd = event.end_time.substring(3, event.end_time.length).toInt()
                                val dateTimeEnd = LocalDateTime.of(date.year, date.monthValue, date.dayOfMonth, hourEnd, minutesEnd)

                                //if user logged time is in range of the lecture time
                                if (parsedDateTime.compareTo(dateTimeStart) == 1 && parsedDateTime.compareTo(dateTimeEnd) == -1) {
                                    pressence = 1
                                    userLoggedDate = parsedDateTime.toLocalTime().toString()
                                }

                                break
                            }
                        }
                    }

                    val eventRes = Event (
                            event.id,
                            event.day,
                            userLoggedDate,
                            event.Name,
                            event.start_time,
                            event.end_time,
                            pressence
                    )
                    events.add(eventRes)
                }
                database.eventDao.insertEvents(events)
            }
            true
        }
    }

    //Subjects
    suspend fun updateSubjects(userId: Long, programId: Int, year: Int, semester: Int) : Boolean {
        return withContext(Dispatchers.IO) {
            //calculate semester based on year
            var realSemester = semester
            if (semester == 1) {
                realSemester = (year*2)-1
            } else {
                realSemester = (year*2)
            }

            val subjectList = NetworkApi.networkService.getSubjectsByProgramAndSemester(programId = programId, semester = realSemester).await()
            val attendances = NetworkApi.networkService.getAttendanceByUserId(userId).await()

            database.subjectDao.clearSubjects()
            if (subjectList.isNotEmpty()) {
                val subjects: MutableList<Subject> = mutableListOf()
                for (subject in subjectList) {
                    var userAttendance = 0
                    var maxAttendance = 0

                    //this is only to fetch each type once for max attendance
                    var bLecture = false
                    var bLab = false
                    var bSeminar = false
                    if (attendances.isNotEmpty()) {
                        for (attendance in attendances) {
                            if (attendance.event!!.subject == subject.id) {
                                userAttendance += 1
                                if (attendance.event!!.max_attendance != null && attendance.event!!.max_attendance != 0) {
                                    if (!bLecture && attendance.event!!.event_type == EventTypeEnum.LECTURE.eventTypeId) {
                                        maxAttendance += attendance.event!!.max_attendance!!
                                        bLecture = true
                                    }
                                    if (!bLab && attendance.event!!.event_type == EventTypeEnum.LAB.eventTypeId) {
                                        maxAttendance += attendance.event!!.max_attendance!!
                                        bLab = true
                                    }
                                    if (!bSeminar && attendance.event!!.event_type == EventTypeEnum.SEMINAR.eventTypeId) {
                                        maxAttendance += attendance.event!!.max_attendance!!
                                        bSeminar = true
                                    }
                                }
                            }
                        }
                    }

                    var percentage = 0
                    if (userAttendance != 0 && maxAttendance != 0) {
                        percentage = ((userAttendance.toDouble()/maxAttendance)*100).toInt()
                    }

                    val subjectRes = Subject (
                            subject.id,
                            subject.Name,
                            subject.program?.shortName,
                            realSemester,
                            programId,
                            "$percentage%"
                    )
                    subjects.add(subjectRes)
                }
                database.subjectDao.insertSubjects(subjects)
            }
            true
        }
    }

    suspend fun getBitmapFromURL(src: String?): Bitmap {
       return withContext(Dispatchers.IO) {
            val url = URL(src)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            val myBitmap = BitmapFactory.decodeStream(input)
            myBitmap
        }
    }

    suspend fun getRoomsByBuildingName(building: String): MutableList<String> {
        return withContext(Dispatchers.IO) {
            val rawRoomList = NetworkApi.networkService.getRoomsByBuildingName(building = building).await()

            if (rawRoomList.isNotEmpty()) {
                val roomList: MutableList<String> = mutableListOf()
                for (room in rawRoomList) {
                    roomList.add(room.name)
                }
                roomList
            } else {
                mutableListOf()
            }
        }
    }

    suspend fun updateSensorData(tab: Int, roomId: Int?, roomName: String?): Boolean {
        return withContext(Dispatchers.IO) {
            var sensorData: List<SensorResponse> = mutableListOf()
            if (roomId == 0) {
                val room: List<Room> = NetworkApi.networkService.getRoomByName(roomName!!).await()
                sensorData = NetworkApi.networkService.getSensorDataByRoomId(room[0].id).await()
            } else {
                sensorData = NetworkApi.networkService.getSensorDataByRoomId(roomId!!).await()
            }

            if (sensorData.isNotEmpty()) {
                //list needs to be sorted by datetime first to pick the newest entries
                sensorData = sensorData.sortedByDescending { it.published_at }

                database.sensorDao.clearSensor(tab)
                val sensorList: MutableList<Sensor> = mutableListOf()
                //used for filtering out duplicates
                val sensorIdList: MutableList<Int> = mutableListOf()
                for (sensor in sensorData) {
                    if (sensorIdList.isEmpty() || !sensorIdList.contains(sensor.sensor!!.id)) {
                        sensorIdList.add(sensor.sensor!!.id)
                        sensorList.add(Sensor(
                                sensor.sensor!!.id,
                                sensor.Results!!.x,
                                sensor.Results!!.y,
                                sensor.Results!!.z,
                                sensor.Results!!.status,
                                sensor.Results!!.Temperature,
                                sensor.Results!!.Humidity,
                                sensor.Results!!.AirPressure,
                                tab
                        ))
                    }
                }

                database.sensorDao.insertSensors(sensorList)
                true
            } else {
                false
            }
        }
    }

    suspend fun getSubjectDetails(subject: Subject, userId: Long) : MutableList<AttendanceDetails> {
        return withContext(Dispatchers.IO) {
            val events = NetworkApi.networkService.getEventsForSubjectId(subject.subjectId).await()
            val attendances = NetworkApi.networkService.getAttendanceByUserId(userId).await()
            val detailsList: MutableList<AttendanceDetails> = mutableListOf()

            if (events.isNotEmpty()) {
                for (event in events) {
                    var userAttendance = 0
                    if (attendances.isNotEmpty()) {
                        for (attendance in attendances) {
                            if (attendance.event!!.id == event.id && attendance.event.event_type == event.event_type!!.id) {
                                userAttendance += 1
                            }
                        }
                    }

                    var maxAtt: Int = 0
                    var minAtt: Int = 0
                    if (event.max_attendance != null && event.max_attendance != 0) {
                        maxAtt = event.max_attendance
                    }
                    if (event.min_attendance != null && event.min_attendance != 0) {
                        minAtt = event.min_attendance
                    }
                    val details = AttendanceDetails (
                            subject.name!!,
                            EventTypeEnum.fromId(event.event_type!!.id)!!.eventName,
                            maxAtt,
                            minAtt, //this needs to be set to the min attendance
                            userAttendance
                    )
                    detailsList.add(details)
                }
            }
            detailsList
        }
    }

    suspend fun getGraphDataBySubject(subjectName: String, userId: Long) : StatsGoalsGraphData? {
        return withContext(Dispatchers.IO) {
            val subjects = NetworkApi.networkService.getSubjectByName(subjectName).await()
            var subjectId = 0
            if (subjects.isNotEmpty()) {
                subjectId = subjects[0].id
            }

            var gData: StatsGoalsGraphData? = null

            if (subjectId != 0) {
                val events = NetworkApi.networkService.getEventsForSubjectId(subjectId).await()
                val attendances = NetworkApi.networkService.getAttendanceBySubjectId(subjectId).await()

                var maxAttendance: Int = 0
                var userAttendance: Int = 0
                var peerAttendance: Int = 0 //total attendances for this subject
                val peers: MutableList<Int> = mutableListOf() //number of different users recorded
                var avgAttendance: Int = 0

                if (events.isNotEmpty()) {
                    //max subject attendance
                    for (event in events) {
                        if (event.max_attendance != null && event.max_attendance != 0) {
                            maxAttendance += event.max_attendance
                        }
                    }

                    if (attendances.isNotEmpty()) {
                        for (attendance in attendances) {
                            //user attendance
                            if (attendance.users_permissions_user?.id == userId.toInt() ) {
                                userAttendance += 1
                            }
                            if (peers.isEmpty() || !peers.contains(attendance.users_permissions_user?.id)) {
                                peers.add(attendance.users_permissions_user?.id!!)
                            }
                            peerAttendance += 1
                        }
                        //avg attendance
                        if (maxAttendance != 0) {
                            if (peerAttendance != 0) {
                                avgAttendance = (((peerAttendance.toDouble()/peers.size)/maxAttendance)*100).toInt()
                            }
                            if (userAttendance != 0) {
                                userAttendance = ((userAttendance.toDouble()/maxAttendance)*100).toInt()
                            }
                        }
                    }
                }
                gData = StatsGoalsGraphData(userAttendance, avgAttendance, maxAttendance)
            }
            gData
        }
    }

    suspend fun getSemestarCompletionData(user :User) :Float{
        return withContext(Dispatchers.IO){
            var currentAttendace : Float = 0F

            val attendaces = NetworkApi.networkService.getAttendanceByUserId(user.userId).await()
            val subjectList = NetworkApi.networkService.getSubjectsByProgramAndSemester(programId = user!!.program!!, semester = user!!.semester!!).await()
            var maxAttendance : Int = 0
            var userAttendance : Int = 0

            //Get user subjects by program and max attandance for that program
            if(subjectList.isNotEmpty()){
                for(subject in subjectList){
                    val events = NetworkApi.networkService.getEventsForSubjectId(subject.id!!).await()
                    for(event in events){
                        maxAttendance += event.max_attendance!!
                    }

                }
            }

            //Get attendance for user
            if(attendaces.isNotEmpty()){
                for(attendace in attendaces){
                    if (attendace.users_permissions_user?.id == user.userId.toInt() ) {
                        userAttendance += 1
                    }
                }
            }
            currentAttendace = userAttendance.toFloat()/maxAttendance.toFloat()
            currentAttendace
        }
    }

    suspend fun myTardiness(user: User) : TardinessData?{
        return  withContext(Dispatchers.IO){

            var data : TardinessData? = null
            var currentAttendance: Int = 0
            var late: Int = 0
            var inTime: Int = 0
            var early: Int = 0

            val attendances = NetworkApi.networkService.getAttendanceByUserId(user.userId).await()
            for( attendance in attendances) {

                currentAttendance += 1
                //Get minutes and hours from subjects and log time from students
                val logMinutes = attendance.log_time.substring(14,16).toInt()
                val logHours = attendance.log_time.substring(11,13).toInt()
                val minutesStart = attendance.event?.start_time!!.substring(attendance.event?.start_time!!.indexOf(':')+1, attendance.event?.start_time!!.length).toInt()
                val hoursStart = attendance.event?.start_time!!.substring(0,attendance.event?.start_time!!.indexOf(':')).toInt()

                    if((minutesStart-logMinutes > 0  && hoursStart == logHours) || hoursStart > logHours){
                        early +=1
                    }
                    else if( (minutesStart - logMinutes < 0 && hoursStart == logHours) || hoursStart < logHours){
                        late += 1
                    }
                    else if ( minutesStart == logMinutes && hoursStart == logHours){
                        inTime += 1
                }
            }
            data = TardinessData(currentAttendance,late,inTime,early)
            data
        }
    }

    suspend fun averageTardiness(user: User): TardinessData?{
        return withContext(Dispatchers.IO){
            var data: TardinessData? = null


            var currentAttendance: Int = 0
            var late: Int = 0
            var inTime: Int = 0
            var early: Int = 0

            val attendances = NetworkApi.networkService.getAttendance().await()
            val subjectList = NetworkApi.networkService.getSubjectsByProgramAndSemester(programId = user!!.program!!, semester = user!!.semester!!).await()

            for(subject in subjectList){
                val events = NetworkApi.networkService.getEventsForSubjectId(subjectId = subject.id!!).await()
                for(event in events){
                    for( attendance in attendances){
                        if(attendance.event?.id!! == event?.id ){

                            currentAttendance += 1
                            //Get minutes and hours from subjects and log time from students
                            val logMinutes = attendance.log_time.substring(14,16).toInt()
                            val logHours = attendance.log_time.substring(11,13).toInt()
                            val minutesStart = attendance.event?.start_time!!.substring(attendance.event?.start_time!!.indexOf(':')+1, attendance.event?.start_time!!.length).toInt()
                            val hoursStart = attendance.event?.start_time!!.substring(0,attendance.event?.start_time!!.indexOf(':')).toInt()

                            if((minutesStart-logMinutes > 0  && hoursStart == logHours) || hoursStart > logHours){
                                early +=1
                            }
                            else if( (minutesStart - logMinutes < 0 && hoursStart == logHours) || hoursStart < logHours){
                                late += 1
                            }
                            else if ( minutesStart == logMinutes && hoursStart == logHours){
                                inTime += 1
                            }
                        }
                    }
                }
            }
            data = TardinessData(currentAttendance,late,inTime,early)
            data
        }
    }
}
