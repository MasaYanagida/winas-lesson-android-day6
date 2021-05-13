package com.winas_lesson.android.day6.Day6Sample.util

fun String.toSafeInt(defValue: Int = 0): Int {
    return try {
        this.toInt()
    } catch (e: Exception) {
        defValue
    }
}

