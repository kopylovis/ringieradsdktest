package com.ope.mobile.android.demo.presentation.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import ch.iagentur.loggeroverlay.LoggerOverlay
import com.ope.mobile.android.OnePlusX
import com.ope.mobile.android.demo.AppConfig
import com.ope.mobile.android.demo.AppConfig.PreferenceDefaultValues.DISPATCH_TIMER_SECONDS
import com.ope.mobile.android.demo.AppConfig.PreferenceDefaultValues.MAX_QUEUE_SIZE
import com.ope.mobile.android.demo.AppConfig.PreferenceKeys.PREFERENCE_KEY_DISPATCH_TIME_SECONDS
import com.ope.mobile.android.demo.AppConfig.PreferenceKeys.PREFERENCE_KEY_MAX_QUEUE_SIZE
import com.ope.mobile.android.demo.R
import com.ope.mobile.android.internal.audiences.AudiencesListener
import com.ope.mobile.android.internal.misc.exceptions.OnePlusXException
import com.ope.mobile.android.public.ConsentType

class StartActivity : AppCompatActivity() {

    companion object {
        private const val PREFERENCE_KEY_CUSTOM_PROPERTY = "CustomPropertyIsSet"
        private const val PREFERENCE_KEY_LOGGER = "showLoggerOverlay"
        const val PREFERENCE_KEY_CONSENT_TYPE = "consentType"
        private const val PREFERENCE_KEY_GIVE_CONSENT = "giveConsent"
    }

    private val preference: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
    }

    private val consentTypeMap = mapOf("TCF v2" to "tcf2", "US Privacy" to "usp", "Implied" to "implied")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        setupLogger()
        setupArticles()
        setupConsent()
        setupCustomProperty()
        setupMaxQueueSize()
        setupDispatchTime()
        setupDisableSdk()
        setupLogin()
        setupAudiences()
    }

    private fun setupMaxQueueSize() {
        findViewById<View>(R.id.acSetMaxQueueSize).setOnClickListener {
            showEnterValueAlertDialog(
                title = "Max queue size",
                initialValue = preference.getLong(PREFERENCE_KEY_MAX_QUEUE_SIZE, MAX_QUEUE_SIZE),
                inputType = InputType.TYPE_CLASS_NUMBER,
                okCallback = { inputText ->
                    preference.edit()
                        .putLong(PREFERENCE_KEY_MAX_QUEUE_SIZE, inputText.toLong())
                        .apply()
                },
                shouldShowRestartDialog = true
            )
        }
    }

    private fun setupLogin() {
        findViewById<View>(R.id.acLogin).setOnClickListener {
            showEnterValueAlertDialog(
                title = "Enter login",
                initialValue = "",
                inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
                okCallback = { inputText ->
                    OnePlusX.login(inputText, AppConfig.PreferenceDefaultValues.LOGIN_SPACE_ID)
                }
            )
        }
        findViewById<View>(R.id.acLogout).setOnClickListener {
            OnePlusX.logout()
        }
    }

    private fun setupDispatchTime() {
        findViewById<View>(R.id.acSetDispatchTimer).setOnClickListener {
            showEnterValueAlertDialog(
                title = "Dispatch time in seconds",
                initialValue = preference.getLong(PREFERENCE_KEY_DISPATCH_TIME_SECONDS, DISPATCH_TIMER_SECONDS),
                inputType = InputType.TYPE_CLASS_NUMBER,
                okCallback = { inputText ->
                    preference.edit()
                        .putLong(PREFERENCE_KEY_DISPATCH_TIME_SECONDS, inputText.toLong())
                        .apply()
                },
                shouldShowRestartDialog = true
            )
        }
    }

    private fun showEnterValueAlertDialog(title: String, initialValue: Any,
                                          okCallback: (inputText: String) -> Unit,
                                          inputType: Int = InputType.TYPE_CLASS_TEXT,
                                          shouldShowRestartDialog: Boolean = false
    ) {
        AlertDialog.Builder(this).apply {
            val input = EditText(this@StartActivity)
            input.setText(initialValue.toString())
            input.inputType = inputType
            setView(input)
            setTitle(title)
            setPositiveButton("OK") { dialog, _ ->
                okCallback(input.text.toString())
                dialog.dismiss()
                if (shouldShowRestartDialog) {
                    showRestartDialog()
                }
            }
            setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        }.show()
    }

    private fun showRestartDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("The change takes effect after app restart")
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }

    private fun setupLogger() {
        val preference = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        if (preference.getBoolean(PREFERENCE_KEY_LOGGER, false)) {
            LoggerOverlay.showLoggerOverlay(this)
        }
        val textView = findViewById<TextView>(R.id.asLoggerTextView)
        textView.text = if (preference.getBoolean(PREFERENCE_KEY_LOGGER, false)) "Hide log overlay" else "Show log overlay"
        findViewById<View>(R.id.asShowLogger).setOnClickListener {
            if (preference.getBoolean(PREFERENCE_KEY_LOGGER, false)) {
                LoggerOverlay.hideLoggerOverlay()
                preference.edit().putBoolean(PREFERENCE_KEY_LOGGER, false).apply()
            } else {
                LoggerOverlay.showLoggerOverlay(this)
                preference.edit().putBoolean(PREFERENCE_KEY_LOGGER, true).apply()
            }
            textView.text = if (preference.getBoolean(PREFERENCE_KEY_LOGGER, false)) "Hide log overlay" else "Show log overlay"
        }
    }

    private fun setupArticles() {
        findViewById<View>(R.id.asArticles).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupConsent() {
        val preference = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        setupConsentValues()
        findViewById<View>(R.id.asGiveConsent).setOnClickListener {
            val wasConsentGiven = preference.getBoolean(PREFERENCE_KEY_GIVE_CONSENT, false)
            preference.edit().putBoolean(PREFERENCE_KEY_GIVE_CONSENT, wasConsentGiven.not()).apply()
            setupConsentValues()
        }
        findViewById<View>(R.id.asSendEvents).setOnClickListener {
            OnePlusX.sendPendingEvents()
        }
        findViewById<View>(R.id.asConsentType).setOnClickListener {
            showChooseConsentTypeDialog()
        }
        val consentTypeTextView = findViewById<TextView>(R.id.asConsentTypeTextView)
        val selectedConsentType = preference.getString(PREFERENCE_KEY_CONSENT_TYPE, null)
        val array = consentTypeMap.keys.toTypedArray()
        var checkedItem = consentTypeMap.values.toList().indexOf(selectedConsentType)
        if (checkedItem == -1) {
            checkedItem = 0
        }
        consentTypeTextView.text = array[checkedItem]
    }

    private fun setupConsentValues() {
        val selectedConsentType = preference.getString(PREFERENCE_KEY_CONSENT_TYPE, null)
        val preference = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val consentTextView = findViewById<TextView>(R.id.asConsentTextView)

        if (ConsentType.TCF_2.typeString == selectedConsentType) {
            preference.edit().remove("IABUSPrivacy_String").apply()
            if (preference.getBoolean(PREFERENCE_KEY_GIVE_CONSENT, false)) {
                preference.edit().putString("IABTCF_TCString", "CPIjnR3PIjnR3FnABAENBOCMAP_AAAAAAAAAHaNf_X_fb39j-_59_9t0eY1f9_7_v-0zjhfds-8Nyf_X_L8X42M7vF36pq4KuR4Eu3LBIQFlHOHUTUmw6okVrTPsak2Mr7NKJ7LEinMbe2dYGHtfn91TuZKYr_7s___z__-__v__79f_r-3_3_vp9X---_e_V399wAAAAAAA.IHbNf_X_fb39j-_59__t0eY1f9_7_v-0zjhfds-8Nyf_X_L8X_2M7vF36pq4KuR4ku3bBIQFtHOnUTUmx6olVrTPsak2Mr7NKJ7Lkmnsbe2dYGHtfn91T-ZKZr_7v___7________79______3_v_____-_____9_8AA").apply()
                preference.edit().putInt("IABTCF_gdprApplies", 1).apply()
            } else {
                preference.edit().remove("IABTCF_TCString").apply()
                preference.edit().remove("IABTCF_gdprApplies").apply()
            }
        } else if (ConsentType.USP.typeString == selectedConsentType) {
            preference.edit().remove("IABTCF_TCString").apply()
            preference.edit().remove("IABTCF_gdprApplies").apply()
            if (preference.getBoolean(PREFERENCE_KEY_GIVE_CONSENT, false)) {
                preference.edit().putString("IABUSPrivacy_String", "1YNN").apply()
            } else {
                preference.edit().remove("IABUSPrivacy_String").apply()
            }
        } else {
            preference.edit().remove("IABUSPrivacy_String").apply()
            preference.edit().remove("IABTCF_TCString").apply()
            preference.edit().remove("IABTCF_gdprApplies").apply()
        }
        consentTextView.text = if (preference.getBoolean(PREFERENCE_KEY_GIVE_CONSENT, false)) "Remove consent" else "Give consent"
    }

    private fun showChooseConsentTypeDialog() {
        val preference = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val selectedConsentType = preference.getString(PREFERENCE_KEY_CONSENT_TYPE, null)
        val array = consentTypeMap.keys.toTypedArray()
        var checkedItem = consentTypeMap.values.toList().indexOf(selectedConsentType)
        if (checkedItem == -1) {
            checkedItem = 0
        }
        var alertDialog: AlertDialog? = null
        alertDialog = AlertDialog.Builder(this)
            .setTitle("Choose consent type")
            .setSingleChoiceItems(array, checkedItem
            ) { _, which ->
                val consentType = consentTypeMap[array[which]]
                preference.edit().putString(PREFERENCE_KEY_CONSENT_TYPE, consentType).apply()
                alertDialog?.dismiss()
                setupConsent()
            }.create()
        alertDialog.show()
    }

    private fun setupCustomProperty() {
        val preference = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val propertyTextView = findViewById<TextView>(R.id.asCustomPropertyTextView)
        propertyTextView.text = if (preference.getBoolean(PREFERENCE_KEY_CUSTOM_PROPERTY, false)) "Remove custom property: (testProperty:testPropertyValue)" else "Set custom property: (testProperty:testPropertyValue)"
        if (preference.getBoolean(PREFERENCE_KEY_CUSTOM_PROPERTY, false)) {
            OnePlusX.setCustomProperty("testProperty", "testPropertyValue")
        }
        findViewById<View>(R.id.acSetCustomProperty).setOnClickListener {
            if (preference.getBoolean(PREFERENCE_KEY_CUSTOM_PROPERTY, false)) {
                preference.edit().putBoolean(PREFERENCE_KEY_CUSTOM_PROPERTY, false).apply()
                OnePlusX.setCustomProperty("testProperty", null)

            } else {
                preference.edit().putBoolean(PREFERENCE_KEY_CUSTOM_PROPERTY, true).apply()
                OnePlusX.setCustomProperty("testProperty", "testPropertyValue")
            }
            propertyTextView.text = if (preference.getBoolean(PREFERENCE_KEY_CUSTOM_PROPERTY, false)) "Remove custom property: (testProperty:testPropertyValue)" else "Set custom property: (testProperty:testPropertyValue)"

        }
    }

    private fun setupDisableSdk() {
        val disableTextView = findViewById<TextView>(R.id.asDisableTextView)
        disableTextView.text = if (OnePlusX.isEnabled(this)) "Disable SDK" else "Enable SDK"
        findViewById<View>(R.id.acDisableSdk).setOnClickListener {
            if (OnePlusX.isEnabled(this)) {
                OnePlusX.disable(this)
            } else {
                OnePlusX.enable(this)
            }
            disableTextView.text = if (OnePlusX.isEnabled(this)) "Disable SDK" else "Enable SDK"
        }
    }

    private fun setupAudiences() {
        findViewById<View>(R.id.asAudiences).setOnClickListener {
            OnePlusX.fetchAudiences("https://test.com", object : AudiencesListener {
                override fun onSuccess(payload: Map<String, Any?>?) {
                    showAlertDialog(payload?.toString() ?: "")
                }

                override fun onError(error: Throwable) {
                    if (error is OnePlusXException) {
                        showAlertDialog("error code = ${error.errorCode}")
                    } else {
                        showAlertDialog(error.message ?: "")
                    }
                }
            })
        }
    }

    private fun showAlertDialog(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("ok", null)
            .show()
    }

}