package com.todo.myapplication.Notification

import android.content.Context
import android.content.SharedPreferences

object MySharedPreferences{

    val PREFS_FILENAME = "prefs"
    val PREF_KEY_EDITTEXT = "notification"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_FILENAME, 0)
    }
    var notification: String
        get() = prefs.getString(PREF_KEY_EDITTEXT, "Y")!!
        set(value) = prefs.edit().putString(PREF_KEY_EDITTEXT, value).apply()

}