package com.winas_lesson.android.day6.Day6Sample.ui

import android.Manifest
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.winas_lesson.android.day6.Day6Sample.App

abstract class AbstractActivity : AppCompatActivity() {
    val isTop: Boolean
        get() {
            return equals(App.topActivity)
        }

    protected var isViewLoaded: Boolean = false
    protected var lastPauseTime: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        isViewLoaded = true
    }

    override fun onResume() {
        super.onResume()
        App.topActivity = this
    }

    override fun onPause() {
        lastPauseTime = (System.currentTimeMillis() / 1000L).toInt()
        if (isTop) App.topActivity = null
        super.onPause()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.repeatCount == 0) {
            onBackPressed()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // TODO
    }
}
