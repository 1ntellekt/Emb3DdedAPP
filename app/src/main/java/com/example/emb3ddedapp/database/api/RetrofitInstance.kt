package com.example.emb3ddedapp.database.api


import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://em3ded.000webhostapp.com/api/"

    private val client = OkHttpClient.Builder()

    private val retrofit by lazy {

        val logging = HttpLoggingInterceptor()

        logging.level = (HttpLoggingInterceptor.Level.BODY)
        client.addInterceptor(logging)

        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client.build())
            .build()
    }

    fun setAuthorizationBearer(token:String){
        val bearer = "Bearer $token"
        client.addInterceptor(Interceptor { chain ->
            val request = chain.request().newBuilder().addHeader("Authorization", bearer).build()
            chain.proceed(request)
        })
    }


    val api:ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }


}