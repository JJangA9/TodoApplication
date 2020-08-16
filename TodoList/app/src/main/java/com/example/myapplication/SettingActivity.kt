package com.example.myapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.Notification.App

class SettingActivity : AppCompatActivity() {

//    lateinit var notificationManager : NotificationManager
//    lateinit var notificationChannel : NotificationChannel
//    lateinit var builder : Notification.Builder
//    private val channelId = "i.apps.notifications"
//    private val description = "Test notification"

    private lateinit var toggle: Switch
    private var toggleInit: String = "N"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        toggle = findViewById<Switch>(R.id.toggleBtn)
        if(App.prefs.notification == "Y") {toggle.isChecked = true}
        else {toggle.isChecked = false}
//        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "설정"
        actionbar.setDisplayHomeAsUpEnabled(true)

        toggle.setOnCheckedChangeListener ({ _, isChecked ->
            toggleInit = if (isChecked) "Y" else "N"
            App.prefs.notification = toggleInit
        })
    }

    //뒤로가기 버튼
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}