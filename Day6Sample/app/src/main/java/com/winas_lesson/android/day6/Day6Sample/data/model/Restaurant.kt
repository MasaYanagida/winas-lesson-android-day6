package com.winas_lesson.android.day6.Day6Sample.data.model

import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class Restaurant(
    var id: Int = 0,
    var name: String = "",
    @Json(name = "category") var categoryId: Int = 0,
    var address: String = "",
    var phone: String = "",
    @Json(name = "photo_url") var photoUrl: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)
