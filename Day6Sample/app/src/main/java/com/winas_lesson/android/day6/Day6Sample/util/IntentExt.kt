package com.winas_lesson.android.day6.Day6Sample.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File

fun Intent.addOutputFileExtra(context: Context, file: File) {
    if (Build.VERSION_CODES.N > Build.VERSION.SDK_INT) {
        putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file))
    } else {
        val photoUri = FileProvider.getUriForFile(
            context, "com.winas_lesson.android.day6.Day6Sample.fileprovider", file
        )
        putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
    }
}
