package com.winas_lesson.android.day6.Day6Sample.data.api

import com.winas_lesson.android.day6.Day6Sample.data.model.Restaurant
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SampleApiService {
    @GET("restaurant-adr.json")
    fun getRestaurant(@Query("restaurant_id") id: Int): Deferred<Response<Restaurant>>
}