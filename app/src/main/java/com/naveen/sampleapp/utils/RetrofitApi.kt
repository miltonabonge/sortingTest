package com.naveen.sampleapp.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitApi {
    private const val BASE_URL = "https://fetch-hiring.s3.amazonaws.com/"
    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    val retrofitService: RetrofitApiService by lazy {
        retrofit().create(RetrofitApiService::class.java)
    }

}