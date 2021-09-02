package ch.iagentur.ringieradsdk.demo.providers

import android.content.Context
import androidx.preference.PreferenceManager
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.DEFAULT_CONFIG
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.EditTextDialog.KEYWORD_KEY_PREF
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.EditTextDialog.KEYWORD_VALUE_PREF
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.EditTextDialog.ADUNIT_ID
import ch.iagentur.ringieradsdk.demo.DemoAppConfig.EditTextDialog.MEMBER_ID
import ch.iagentur.ringieradsdk.demo.extensions.saveBoolean
import ch.iagentur.ringieradsdk.demo.extensions.saveString
import ch.iagentur.ringieradsdk.external.RingierConfig.URLS_SETTINGS
import ch.iagentur.ringieradsdk.external.models.RingierAdUrlModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RingierAdPreferences(private val context: Context) {

    private val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)

    var isTestModeEnabled: Boolean
        set(value) = sharedPrefs.saveBoolean(KEY_TEST_SETTINGS, value = value)
        get() = sharedPrefs.getBoolean(KEY_TEST_SETTINGS, false)

    var isFirstAppLaunch: Boolean
        set(value) = sharedPrefs.saveBoolean(KEY_FIRST_LAUNCH, value = value)
        get() = sharedPrefs.getBoolean(KEY_FIRST_LAUNCH, true)

    var memberIdPref: String
        set(value) = sharedPrefs.saveString(MEMBER_ID, value = value)
        get() = sharedPrefs.getString(MEMBER_ID, "").toString()

    var adUnitIdPref: String
        set(value) = sharedPrefs.saveString(ADUNIT_ID, value = value)
        get() = sharedPrefs.getString(ADUNIT_ID, "").toString()

    var keywordKeyPref: String
        set(value) = sharedPrefs.saveString(KEYWORD_KEY_PREF, value = value)
        get() = sharedPrefs.getString(KEYWORD_KEY_PREF, "").toString()

    var keywordValuePref: String
        set(value) = sharedPrefs.saveString(KEYWORD_VALUE_PREF, value = value)
        get() = sharedPrefs.getString(KEYWORD_VALUE_PREF, "").toString()

    fun removeString(key: String) {
        sharedPrefs.edit().remove(key).apply()
    }

    fun saveUrlsList(listRingierModel: List<RingierAdUrlModel>) {
        val gson = Gson()
        val editor = sharedPrefs.edit()
        val jsonString = gson.toJson(listRingierModel)
        editor.putString(URLS_SETTINGS, jsonString).apply()
    }

    fun getUrlsList(): List<RingierAdUrlModel> {
        val gson = Gson()
        val jsonString = sharedPrefs.getString(URLS_SETTINGS, null)
        val type = object : TypeToken<List<RingierAdUrlModel?>?>() {}.type
        if (jsonString != null) {
            return gson.fromJson(jsonString, type)
        }
        return emptyList()
    }

    fun getUrl(): String {
        val urls = getUrlsList()
        val filtered = urls.filter {
            it.isActivated
        }
        if (filtered.isEmpty()) {
            return DEFAULT_CONFIG
        }
        return filtered.last().url
    }

    companion object {
        private const val KEY_TEST_SETTINGS = "TEST_SETTINGS"
        private const val KEY_FIRST_LAUNCH = "KEY_FIRST_LAUNCH"
    }

}