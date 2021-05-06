package com.winas_lesson.android.day6.Day6Sample

import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.multidex.MultiDex
import com.winas_lesson.android.day6.Day6Sample.ui.AbstractActivity
import timber.log.Timber

class App : Application() {

    companion object {
        lateinit var context: Context
        var topActivity: AbstractActivity? = null
    }

    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext
        Timber.plant(AppDebugTree())
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}

class AppDebugTree : Timber.DebugTree() {

    private val MAX_LOG_LENGTH = 4000
    private var callerInfo: String = ""

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
        val thread = Throwable().stackTrace

        callerInfo = if (thread != null && thread.size >= 5) {
            formatForLogCat(thread[5])
        } else ""

        if (message.length < MAX_LOG_LENGTH) {
            printSingleLine(priority, tag ?: "", message + callerInfo);
        } else {
            printMultipleLines(priority, tag ?: "", message);
        }
    }

    private fun printMultipleLines(priority: Int, tag: String, message: String) {
        // Split by line, then ensure each line can fit into Log's maximum length.
        var i = 0
        val length = message.length
        while (i < length) {
            var newline = message.indexOf('\n', i)
            newline = if (newline != -1) newline else length
            do {
                val end = Math.min(newline, i + MAX_LOG_LENGTH)
                val part = message.substring(i, end)
                printSingleLine(priority, tag, part)
                i = end
            } while (i < newline)
            i++
        }

        if (!TextUtils.isEmpty(callerInfo)) {
            printSingleLine(priority, tag, callerInfo)
        }
    }

    private fun printSingleLine(priority: Int, tag: String, message: String) {
        if (priority == Log.ASSERT) {
            Log.wtf(tag, message)
        } else {
            Log.println(priority, tag, message)
        }
    }

    private fun formatForLogCat(stack: StackTraceElement): String {
        val className = stack.className
        val packageName = className.substring(0, className.lastIndexOf("."))
        return String.format(" at %s(%s:%s)", packageName, stack.fileName, stack.lineNumber)
    }
}
