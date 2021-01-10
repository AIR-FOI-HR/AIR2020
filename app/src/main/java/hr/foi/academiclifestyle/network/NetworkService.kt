package hr.foi.academiclifestyle.network

import android.media.Image
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hr.foi.academiclifestyle.network.model.*
import kotlinx.coroutines.Deferred
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.io.File

private const val BASE_URL = "https://air-analyzer.herokuapp.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

interface DatabaseApiService {
    @POST("auth/local/")
    fun postLogin(@Body body: LoginRequest?): Deferred<Response>

    @POST("auth/local/register")
    fun postRegister(@Body body: RegisterRequest?): Deferred<Response>

    @GET("users/me")
    fun getUserByToken(@Header("Authorization") authorization: String): Deferred<UserResponse>

    @PUT("users/me")
    fun updateUser(@Header("Authorization") authorization: String,
                   @Body body: UserRequest?) : Deferred<UserResponse>
  
    @GET("events")
    fun getEventsForDayAndProgram(@Query("subject.program") programId: Int, @Query("day") day: String): Deferred<List<Event>>

    @GET("subjects")
    fun getSubjectsByProgramAndSemester(@Query("program.id") programId: Int, @Query("Semester") semester: Int): Deferred<List<SubjectProgram>>

    @Multipart
    @POST ("upload")
    fun uploadPicture(@Header("Authorization") authorization: String,
                      @Part files : MultipartBody.Part) : Deferred<List<ImageResponse>>

    @GET("rooms")
    fun getRoomsByBuildingName(@Query("building.name") building: String): Deferred<List<Room>>
}

object NetworkApi {
    //.addConverterFactory(ScalarsConverterFactory.create())
    //.addConverterFactory(GsonConverterFactory.create())
    //.addConverterFactory(MoshiConverterFactory.create(moshi))
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

    val networkService = retrofit.create(
        DatabaseApiService::class.java)
}