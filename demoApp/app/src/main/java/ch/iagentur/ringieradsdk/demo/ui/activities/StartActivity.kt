package ch.iagentur.ringieradsdk.demo.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.preference.PreferenceManager
import ch.iagentur.loggeroverlay.LoggerOverlay
import ch.iagentur.ringieradsdk.BuildConfig
import ch.iagentur.ringieradsdk.demo.databinding.ActivityStartBinding
import ch.iagentur.ringieradsdk.demo.ui.ViewBindingBaseActivity
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.distribute.Distribute
import com.microsoft.appcenter.distribute.UpdateTrack

class StartActivity : ViewBindingBaseActivity<ActivityStartBinding>() {

    companion object {
        private const val PREFERENCE_KEY_LOGGER = "showLoggerOverlay"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLogger()
        setupArticles()
        setupAppCenter()
        setupVersions()
        setupSettings()
        setupToolbar()
    }

    private fun setupToolbar() {
        title = "RingierAd Demo App"
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun setupLogger() {
        val preference = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        if (preference.getBoolean(PREFERENCE_KEY_LOGGER, false)) {
            LoggerOverlay.showLoggerOverlay(this)
        }
        binding?.apply {
            asLoggerTextView.text = if (preference.getBoolean(PREFERENCE_KEY_LOGGER, false)) "Hide log overlay" else "Show log overlay"
            asShowLogger.setOnClickListener {
                if (preference.getBoolean(PREFERENCE_KEY_LOGGER, false)) {
                    LoggerOverlay.hideLoggerOverlay()
                    preference.edit().putBoolean(PREFERENCE_KEY_LOGGER, false).apply()
                } else {
                    LoggerOverlay.showLoggerOverlay(this@StartActivity)
                    preference.edit().putBoolean(PREFERENCE_KEY_LOGGER, true).apply()
                }
                asLoggerTextView.text = if (preference.getBoolean(PREFERENCE_KEY_LOGGER, false)) "Hide log overlay" else "Show log overlay"
            }
        }
    }

    private fun setupArticles() {
        binding?.asArticles?.setOnClickListener {
            val intent = Intent(this, ArticlesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupAppCenter() {
        if (BuildConfig.DEBUG) {
            Distribute.setEnabledForDebuggableBuild(true)
            Distribute.setUpdateTrack(UpdateTrack.PRIVATE)
            AppCenter.start(application, "a3665f0f-eecd-44b0-ac29-6e3454edeba7",
                Analytics::class.java,
                Crashes::class.java,
                Distribute::class.java)
        }
    }

    private fun setupSettings() {
        binding?.asSettings?.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupVersions() {
        binding?.apply {
            asSdkVersion.text = ("SDK version: ${BuildConfig.SDK_VERSION}")
            val info = packageManager.getPackageInfo(packageName, 0)
            asAppVersion.text = ("App version: ${info.versionName}.${info.versionCode}")
        }
    }

    override val bindingInflater: (LayoutInflater) -> ActivityStartBinding
        get() = ActivityStartBinding::inflate

}