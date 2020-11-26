package hr.foi.academiclifestyle.database

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hr.foi.academiclifestyle.data.models.LoginRequest
import hr.foi.academiclifestyle.data.models.AuthResponse
import hr.foi.academiclifestyle.data.models.RegisterRequest
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://api-airanalyzer.oa.r.appspot.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//.addConverterFactory(ScalarsConverterFactory.create())
//.addConverterFactory(GsonConverterFactory.create())
//.addConverterFactory(MoshiConverterFactory.create(moshi))
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface DatabaseApiService {
    @POST("auth/local/")
    fun postLogin(@Body body: LoginRequest?): Deferred<AuthResponse>

    @POST("auth/local/register")
    fun postRegister(@Body body: RegisterRequest?): Deferred<AuthResponse>
}

object DatabaseApi {
    val retrofitService: DatabaseApiService by lazy {
        retrofit.create(DatabaseApiService::class.java)
    }
}