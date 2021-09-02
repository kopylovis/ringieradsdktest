package com.ope.mobile.android.demo

import android.app.Application
import androidx.preference.PreferenceManager
import ch.iagentur.loggeroverlay.LoggerOverlay
import ch.iagentur.loggeroverlay.internal.AlviOptions
import com.ope.mobile.android.OnePlusX
import com.ope.mobile.android.demo.presentation.Logger
import com.ope.mobile.android.demo.presentation.ui.StartActivity
import com.ope.mobile.android.internal.model.QueueDispatchConfig
import com.ope.mobile.android.public.RemoteConfigDefaults
import timber.log.Timber


class DemoApp : Application() {

    companion object {
        const val CLIENT_ID = "acme"
    }
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Logger)
        }

        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val selectedConsentType = preferences.getString(StartActivity.PREFERENCE_KEY_CONSENT_TYPE, null)
        val config = RemoteConfigDefaults(
            shouldLogToConsole = BuildConfig.DEBUG,
            queueDispatchConfig = QueueDispatchConfig(
                seconds = preferences.getLong(
                    AppConfig.PreferenceKeys.PREFERENCE_KEY_DISPATCH_TIME_SECONDS,
                    AppConfig.PreferenceDefaultValues.DISPATCH_TIMER_SECONDS
                ),
                maxSize = preferences.getLong(
                    AppConfig.PreferenceKeys.PREFERENCE_KEY_MAX_QUEUE_SIZE,
                    AppConfig.PreferenceDefaultValues.MAX_QUEUE_SIZE
                )
            ),
            consentType = selectedConsentType
        )
        OnePlusX.start(this, CLIENT_ID, config, AppConfig.REMOTE_CONFIG_URL)
        setupLoggerOverlay()
    }

    private fun setupLoggerOverlay() {
        val config = LoggerOverlay.Config(BuildConfig.DEBUG, AlviOptions.Builder().build())
        LoggerOverlay.init(this, config)
        LoggerOverlay.addLogger(Logger.loggerOverlay)
    }

}