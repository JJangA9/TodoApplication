package com.example.myapplication.Notification

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {

    val PREFS_FILENAME = "prefs"
    val PREF_KEY_EDITTEXT = "notification"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var notification: String
    get() = prefs.getString(PREF_KEY_EDITTEXT, "1")
    set(value) = prefs.edit().putString(PREF_KEY_EDITTEXT, value).apply()
}