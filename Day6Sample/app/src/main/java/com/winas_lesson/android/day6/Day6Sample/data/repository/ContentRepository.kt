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

    }
}