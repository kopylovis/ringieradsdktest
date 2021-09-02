package ch.iagentur.ringieradsdk.demo.misc

import android.os.Handler
import android.os.Looper
import ch.iagentur.loggeroverlay.internal.ILoggerOverlay
import ch.iagentur.ringieradsdk.external.RingierConfig
import timber.log.Timber

object Logger : Timber.DebugTree() {

    private val handler = Handler(Looper.getMainLooper())
    val loggerOverlay = object : ILoggerOverlay() {
        override fun getType(): String {
            return "RingierAdSdk"
        }
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
        if (RingierConfig.LOG_TAG == tag) {
            handler.post {
                loggerOverlay.logMessage(message)
            }
        }
    }

}