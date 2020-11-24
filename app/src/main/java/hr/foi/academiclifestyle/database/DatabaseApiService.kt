package hr.foi.academiclifestyle.database

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hr.foi.academiclifestyle.data.models.LoginRequest
import hr.foi.academiclifestyle.data.models.AuthResponse
import hr.foi.academiclifestyle.data.models.RegisterRequest
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "http://10.0.2.2:1337/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//.addConverterFactory(ScalarsConverterFactory.create())
//.addConverterFactory(GsonConverterFactory.create())
//.addConverterFactory(MoshiConverterFactory.create(moshi))
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface DatabaseApiService {
    @POST("auth/local/")
    fun postLogin(@Body body: LoginRequest?): Call<AuthResponse>

    @POST("auth/local/register")
    fun postRegister(@Body body: RegisterRequest?): Call<AuthResponse>
}

object DatabaseApi {
    val retrofitService: DatabaseApiService by lazy {
        retrofit.create(DatabaseApiService::class.java)
    }
}