package com.example.myapplication.Notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import com.example.myapplication.MainActivity
import com.example.myapplication.R

class Notification: BroadcastReceiver(){

    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder

    override fun onReceive(context: Context, intent: Intent) {
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //알람을 추가할 목적이면
        if(intent.getIntExtra("delete", 0) == 0) {
            val iOfIcon = intent.getIntExtra("indexOfIcon", 0)
            val schedule = intent.getStringExtra("schedule")
            val id = intent.getLongExtra("id", 0)

            val notificationIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, id.toInt(), notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = "i.apps.notifications"
                val description = "Test notification"

                notificationChannel = NotificationChannel(
                        channelId, description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(false)
                notificationManager.createNotificationChannel(notificationChannel)

                if (iOfIcon == 0 || iOfIcon == 2) {
                    builder = Notification.Builder(context, channelId)
                            .setSmallIcon(R.drawable.book)
                            .setLargeIcon(BitmapFactory.decodeResource(context.resources,
                                    R.drawable.heart))
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setContentTitle("D - Day")
                            .setContentText(schedule)
                } else if (iOfIcon == 3) {
                    builder = Notification.Builder(context, channelId)
                            .setSmallIcon(R.drawable.book)
                            .setLargeIcon(BitmapFactory.decodeResource(context.resources,
                                    R.drawable.travel))
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setContentTitle("D - Day")
                            .setContentText(schedule)
                } else if (iOfIcon == 4) {
                    builder = Notification.Builder(context, channelId)
                            .setSmallIcon(R.drawable.book)
                            .setLargeIcon(BitmapFactory.decodeResource(context.resources,
                                    R.drawable.conference))
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setContentTitle("D - Day")
                            .setContentText(schedule)
                } else if (iOfIcon == 5) {
                    builder = Notification.Builder(context, channelId)
                            .setSmallIcon(R.drawable.book)
                            .setLargeIcon(BitmapFactory.decodeResource(context.resources,
                                    R.drawable.dinner))
                            .setContentIntent(pendingIntent)
                            .setContentTitle("D - Day")
                            .setContentText(schedule)
                } else {
                    builder = Notification.Builder(context, channelId)
                            .setSmallIcon(R.drawable.book)
                            .setLargeIcon(BitmapFactory.decodeResource(context.resources,
                                    R.drawable.book))
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setContentTitle("D - Day")
                            .setContentText(schedule)
                }
            }  else {
                builder = Notification.Builder(context)
                        .setSmallIcon(R.drawable.book)
                        .setLargeIcon(BitmapFactory.decodeResource(context.resources,
                                R.drawable.book))
                        .setAutoCancel(true)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setContentTitle("D - Day")
                        .setContentText(schedule)
            }
            notificationManager.notify(id.toInt(),builder.build())
        } else { //알람을 안받도록 설정했다면
            val id = intent.getLongExtra("id", 0)
            //val tag =
            notificationManager.cancel("TAG", id.toInt())
        }

    }
}