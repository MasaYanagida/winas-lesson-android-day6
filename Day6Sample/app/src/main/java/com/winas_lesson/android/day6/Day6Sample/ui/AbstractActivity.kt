package com.winas_lesson.android.day6.Day6Sample.ui

import android.Manifest
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.winas_lesson.android.day6.Day6Sample.App
import com.winas_lesson.android.day6.Day6Sample.helper.ContactManager
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
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
        onRequestPermissionsResult(requestCode, grantResults)
    }

    // 許可必要な呼び出し関数
    @NeedsPermission(Manifest.permission.READ_CONTACTS)
    open fun getContacts() {
        ContactManager.shared.fetch(this)
    }

    // Rationaleを見せるところ
    @OnShowRationale(Manifest.permission.READ_CONTACTS)
    open fun showRationaleForContacts(request: PermissionRequest) {
        AlertDialog.Builder(this)
            .setPositiveButton("OK") { _, _ -> request.proceed() }
            .setNegativeButton("NO") { _, _ -> request.cancel() }
            .setCancelable(false)
            .setMessage("連絡先へのアクセスを許可してください。")
            .show()
    }
}
