package ch.iagentur.ringieradsdk.demo

import android.app.Application
import ch.iagentur.loggeroverlay.LoggerOverlay
import ch.iagentur.loggeroverlay.internal.AlviOptions
import ch.iagentur.ringieradsdk.RingierAd
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.EditTextDialog.KEYWORD_KEY_DEFAULT
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.EditTextDialog.KEYWORD_VALUE_DEFAULT
import ch.iagentur.ringieradsdk.demo.misc.Logger
import ch.iagentur.ringieradsdk.demo.providers.RingierAdPreferences
import ch.iagentur.ringieradsdk.external.RingierConfig
import timber.log.Timber

class DemoApp : Application() {

    private lateinit var ringierAdPreferences: RingierAdPreferences

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Logger)
        }
        ringierAdPreferences = RingierAdPreferences(applicationContext)
        val keywords = mutableMapOf<String, String>()
        if (ringierAdPreferences.keywordKeyPref.isNotEmpty()) {
            keywords[ringierAdPreferences.keywordKeyPref] = ringierAdPreferences.keywordValuePref
        }
        setupLoggerOverlay()
        setupDefaultValues()
        Timber.tag(RingierConfig.LOG_TAG).d("Demo app!!: Started")
        val configuration = RingierAd.Configuration(
            configAssetPath = DemoAppConfig.DEMO_TAG_MANAGER_ASSETS_PATH,
            logsEnabled = BuildConfig.DEBUG, ringierAdPreferences.getUrl()
        )
        val customConfiguration = RingierAd.CustomConfiguration(
            memberId = ringierAdPreferences.memberIdPref,
            adUnitId = ringierAdPreferences.adUnitIdPref,
            keywords = keywords,
            isTestMode = ringierAdPreferences.isTestModeEnabled
        )
        Timber.tag(RingierConfig.LOG_TAG).e("Demo app!!: Test mode = ${ringierAdPreferences.isTestModeEnabled}")
        RingierAd.setCustomConfiguration(customConfiguration)
        RingierAd.start(this, configuration)
    }

    private fun setupDefaultValues() {
        if (ringierAdPreferences.isFirstAppLaunch) {
            ringierAdPreferences.isFirstAppLaunch = false
            ringierAdPreferences.keywordKeyPref = KEYWORD_KEY_DEFAULT
            ringierAdPreferences.keywordValuePref = KEYWORD_VALUE_DEFAULT
        }
    }

    private fun setupLoggerOverlay() {
        val config = LoggerOverlay.Config(BuildConfig.DEBUG, AlviOptions.Builder().build())
        LoggerOverlay.init(this, config)
        LoggerOverlay.addLogger(Logger.loggerOverlay)
    }

}