package hr.foi.academiclifestyle.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hr.foi.academiclifestyle.network.model.LoginRequest
import hr.foi.academiclifestyle.network.model.Response
import hr.foi.academiclifestyle.network.model.RegisterRequest
import hr.foi.academiclifestyle.network.model.UserResponse
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://api-airanalyzer.oa.r.appspot.com/"

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