package com.winas_lesson.android.day6.Day6Sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.winas_lesson.android.day6.Day6Sample.data.model.ContactUser
import com.winas_lesson.android.day6.Day6Sample.data.model.Photo
import com.winas_lesson.android.day6.Day6Sample.data.repository.Repository
import com.winas_lesson.android.day6.Day6Sample.interfaces.ViewBindable
import com.winas_lesson.android.day6.Day6Sample.databinding.ActivityMainBinding
import com.winas_lesson.android.day6.Day6Sample.helper.ContactManager
import com.winas_lesson.android.day6.Day6Sample.helper.ContactManagerListener
import com.winas_lesson.android.day6.Day6Sample.interfaces.MediaPickable
import com.winas_lesson.android.day6.Day6Sample.interfaces.MediaType
import com.winas_lesson.android.day6.Day6Sample.ui.AbstractActivity
import com.winas_lesson.android.day6.Day6Sample.util.showToast
import com.winas_lesson.android.day6.Day6Sample.util.toSafeInt
import timber.log.Timber
import kotlin.properties.Delegates

class MainActivity : AbstractActivity(), ViewBindable, MediaPickable {
    override lateinit var binding: ViewBinding
    private val imageView: ImageView?
        get() = (binding as? ActivityMainBinding)?.imageView
    private val counterButton: Button?
        get() = (binding as? ActivityMainBinding)?.counterButton
    private val counterTextView: TextView?
        get() = (binding as? ActivityMainBinding)?.counterTextView
    private val albumButton: Button?
        get() = (binding as? ActivityMainBinding)?.albumButton
    private val contactButton: Button?
        get() = (binding as? ActivityMainBinding)?.contactButton

    private var counter: Int by Delegates.observable(0, { _, _, new ->
        counterTextView?.text = "カウンターは、${new}です"
    })

    companion object {
        private const val PROP_COUNTER = "PROP_COUNTER"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(PROP_COUNTER, counter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        albumButton?.setOnClickListener {
            showAlbum()
        }

        contactButton?.setOnClickListener {
            // TODO
        }

        val restaurantIds = listOf(1, 2, 3, 4, 5)

        Repository.content.getRestaurants(
            restaurantIds = restaurantIds,
            completion = { restaurants ->
                restaurants.forEach {
                    Timber.d("#issue getRestaurants complete!! ID= ${it.id}, name= ${it.name}")
                }
            }
        )

        //counter = 0
        counter = savedInstanceState?.getInt(PROP_COUNTER) ?: 0
        counterButton?.setOnClickListener {
            counter += 1
        }

        contactButton?.setOnClickListener {
            ContactManager.shared.listener = object : ContactManagerListener {
                override fun onFetchUsers(users: List<ContactUser>) {
                    users.firstOrNull()?.let { firstUser ->
                        showToast("連絡先１件めの電話番号は、${firstUser.phoneNumber}です")
                    }
                }
            }
            ContactManager.shared.start(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ActivityRequestCode.ALBUM.code -> {
                if (resultCode == Activity.RESULT_OK) {
                    onGetImageFromAlbum(this, data)
                }
            }
        }
    }

    override var mediaType: MediaType = MediaType.PHOTO
    override var medium: Photo? by Delegates.observable(null, { _, _, new ->
        medium?.let { medium ->
            imageView?.let {
                Glide.with(applicationContext)
                    .load(medium.fileUri)
                    .into(it)
            }
            return@observable
        }
        imageView?.setImageDrawable(null)
    })
}