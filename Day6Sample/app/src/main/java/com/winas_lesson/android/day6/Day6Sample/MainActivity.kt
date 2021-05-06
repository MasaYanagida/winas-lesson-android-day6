package com.winas_lesson.android.day6.Day6Sample

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.winas_lesson.android.day6.Day6Sample.data.model.ContactUser
import com.winas_lesson.android.day6.Day6Sample.data.repository.Repository
import com.winas_lesson.android.day6.Day6Sample.interfaces.ViewBindable
import com.winas_lesson.android.day6.Day6Sample.databinding.ActivityMainBinding
import com.winas_lesson.android.day6.Day6Sample.ui.AbstractActivity
import com.winas_lesson.android.day6.Day6Sample.util.showToast
import timber.log.Timber
import kotlin.properties.Delegates

class MainActivity : AbstractActivity(), ViewBindable {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        albumButton?.setOnClickListener {
            // TODO
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

        counter = 0
        counterButton?.setOnClickListener {
            counter += 1
        }
    }
}