package ch.iagentur.ringieradsdk.demo.extensions

import android.content.SharedPreferences

fun SharedPreferences.saveBoolean(key: String, value: Boolean) {
    edit().putBoolean(key, value).apply()
}

fun SharedPreferences.saveString(key: String, value: String) {
    edit().putString(key, value).apply()
}