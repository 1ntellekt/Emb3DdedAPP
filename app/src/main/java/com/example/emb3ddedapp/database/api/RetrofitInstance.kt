package com.example.emb3ddedapp.database.api


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    //private const val BASE_URL = "https://em3ded.000webhostapp.com/api/"
    //private const val BASE_URL = "https://emb3dapi-5a26a.ondigitalocean.app/api/"
    private const val BASE_URL = "https://www.emb3ded.kz/api/"
    //private const val BASE_URL = "http://192.168.40.126/api/"
   // private const val BASE_URL = "http://emb3dedapi.loc/api/"

    private var token: String = ""
    private val client = OkHttpClient.Builder()

    private val retrofit by lazy {

        val logging = HttpLoggingInterceptor()

        logging.level = (HttpLoggingInterceptor.Level.BODY)
        client.addInterceptor(logging).addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Content-Type","application/json" )
                .addHeader("Accept","application/json")
                .addHeader("Authorization", token)
                .build()

            chain.proceed(request)
        }

        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client.build())
            .build()
    }

    fun setAuthorizationBearer(token:String){
        this.token = "Bearer $token"
    }


    val api:ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }


}