package com.naveen.sampleapp.utils

import com.naveen.sampleapp.model.Item
import retrofit2.Response
import retrofit2.http.GET


interface RetrofitApiService {

    @GET("hiring.json")
    suspend fun getItemsList(): Response<List<Item>>

}