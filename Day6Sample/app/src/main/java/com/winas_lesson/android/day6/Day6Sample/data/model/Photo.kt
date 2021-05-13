package com.winas_lesson.android.day6.Day6Sample.data.model

import android.graphics.Bitmap
import android.net.Uri

data class Photo(
    var fileUri: Uri = Uri.EMPTY,
    var thumbnail: Bitmap? = null
)
