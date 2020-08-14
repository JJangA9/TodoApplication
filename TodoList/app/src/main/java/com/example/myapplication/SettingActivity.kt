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

class SettingActivity : AppCompatActivity() {

    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"

    private lateinit var toggle: Switch
    private var toggleInit: String = "N"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "설정"
        actionbar.setDisplayHomeAsUpEnabled(true)

        toggle = findViewById<Switch>(R.id.toggleBtn)
        toggle.setOnCheckedChangeListener ({ _, isChecked ->
            toggleInit = if (isChecked) "Y" else "N"
            addNotification(toggleInit)
        })
    }

    private fun addNotification(toggleInit: String) {
        if(toggleInit == "Y") {
            val notificationIntent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(
                        channelId,description,NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(false)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(this,channelId)
                        .setSmallIcon(R.drawable.book)
                        .setLargeIcon(BitmapFactory.decodeResource(this.resources,
                                R.drawable.heart))
                        .setContentIntent(pendingIntent)
                        .setContentTitle("투두")
                        .setContentText("테스트 알림")
            }else{

                builder = Notification.Builder(this)
                        .setSmallIcon(R.drawable.todoaddbtn)
                        .setLargeIcon(BitmapFactory.decodeResource(this.resources,
                                R.drawable.ic_launcher_background))
                        .setContentIntent(pendingIntent)
                        .setContentTitle("투두")
                        .setContentText("테스트 알림")
            }
            notificationManager.notify(1234,builder.build())
        }
    }

    //뒤로가기 버튼
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}