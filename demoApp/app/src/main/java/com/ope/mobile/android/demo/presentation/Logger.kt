package com.ope.mobile.android.demo.presentation

import android.os.Handler
import android.os.Looper
import ch.iagentur.loggeroverlay.internal.ILoggerOverlay
import com.ope.mobile.android.Config
import timber.log.Timber

object Logger : Timber.DebugTree() {

    private val handler = Handler(Looper.getMainLooper())
    val loggerOverlay = object : ILoggerOverlay() {
        override fun getType(): String {
            return "OnePlusX"
        }
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
        if (Config.TAG == tag) {
            handler.post {
                loggerOverlay.logMessage(message)
            }
        }
    }

}