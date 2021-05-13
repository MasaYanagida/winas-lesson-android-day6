package com.winas_lesson.android.day6.Day6Sample.data.repository

import android.os.Handler
import android.os.Looper
import com.winas_lesson.android.day6.Day6Sample.data.model.Restaurant
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ContentRepository {
    fun getRestaurants(
        restaurantIds: List<Int>,
        completion: (restaurants: List<Restaurant>) -> Unit
    ) {
        val getRestaurantAsync: (
            restaurantId: Int,
            completion: (restaurant: Restaurant?) -> Unit
        ) -> Deferred<String> = { restaurantId, completion ->
            val response = Repository.apiInterface.getRestaurant(restaurantId)
            GlobalScope.async {
                response.await()
                suspendCoroutine { continuation ->
                    val response = response.getCompleted()
                    //Timber.d("#issue getRestaurant!! ID= ${restaurantId}")
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data != null) {
                            data.id = restaurantId
                            completion(data)
                            continuation.resume("completion")
                        } else {
                            completion(null)
                            continuation.resume("failure")
                        }
                    } else {
                        completion(null)
                        continuation.resume("failure")
                    }
                }
            }
        }

        val restaurants: Array<Restaurant?> = arrayOfNulls<Restaurant?>(restaurantIds.size)
        //val restaurants: MutableList<Restaurant?> = mutableListOf()
        val processList: ArrayList<Deferred<String>> = arrayListOf()
        restaurantIds.forEachIndexed { offset, restaurantId ->
            // coroutine
            processList.add(getRestaurantAsync(restaurantId) {
                //restaurants.add(it)
                restaurants[offset] = it
            })
        }

        GlobalScope.async {
            processList.awaitAll()
            Handler(Looper.getMainLooper()).post {
                completion(restaurants.filterNotNull())
            }
        }
    }
}