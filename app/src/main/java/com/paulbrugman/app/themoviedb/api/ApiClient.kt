package com.paulbrugman.app.themoviedb.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // Add logging interceptor for debug version
    fun getClient(config: String): APIServices {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        // Retrofit client based on custom OkHttpClient
        val retrofitClient = Retrofit
            .Builder()
            .baseUrl(config)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Api services for all end-points
        return retrofitClient.create(APIServices::class.java)
    }
}