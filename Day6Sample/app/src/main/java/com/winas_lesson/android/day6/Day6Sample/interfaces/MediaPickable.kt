package com.winas_lesson.android.day6.Day6Sample.interfaces

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import com.winas_lesson.android.day6.Day6Sample.ActivityRequestCode
import com.winas_lesson.android.day6.Day6Sample.data.model.Photo
import com.winas_lesson.android.day6.Day6Sample.ui.AbstractActivity

enum class MediaType(val key: Int) {
    PHOTO(1);

    val mediaTypes: List<Int>
        get() {
            return when (this) {
                PHOTO -> listOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
            }
        }
    val pickerIntent: Intent
        get() {
            return when (this) {
                PHOTO -> {
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                }
            }
        }
}

interface MediaPickable {
    var mediaType: MediaType
    var medium: Photo?

    enum class TYPE(val key: Int) {
        ALBUM(0);
        fun action(pickable: MediaPickable): () -> Unit {
            val closure: () -> Unit
            when (this) {
                ALBUM -> {
                    closure = {
                        val intent = pickable.mediaType.pickerIntent
                        var activity = pickable as? AbstractActivity
                        activity?.startActivityForResult(intent, ActivityRequestCode.ALBUM.code)
                    }
                }
            }
            return closure
        }
    }

    fun showAlbum() {
        TYPE.ALBUM.action(this).invoke()
    }
    fun onGetImageFromAlbum(context: Context, intent: Intent?) {
        intent?.let { intent ->
            try {
                intent.data?.let { uri ->
                    val cursor = context.contentResolver.query(
                        uri, null, null, null, null
                    )
                    if (cursor != null && cursor.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)
                        val mimeType = cursor.getString(columnIndex)
                        cursor.close()
                        if (mimeType.contains("image", true)) {
                            val photo = Photo(fileUri = uri)
                            medium = photo
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
