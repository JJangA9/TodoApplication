package com.todo.myapplication.Notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.todo.myapplication.RoomDB.Schedule
import java.util.*

class AlarmSetting(context: Context) {

    private val context: Context
    lateinit var sender: PendingIntent
    lateinit var calendar: Calendar
    lateinit var date: String
    val am: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    init{ this.context = context}

    fun makeAlarm(alarmData: MutableList<Schedule>) {

        val intent = Intent(context, Notification::class.java)

        //알람에 띄울 아이콘 index와 일정 내용 보내기
        for(x in 1..alarmData.size) {
            intent.putExtra("indexOfIcon", alarmData[x-1].iconIndex)
            intent.putExtra("schedule", alarmData[x-1].schedule)
            intent.putExtra("id", alarmData[x-1].id)
            intent.putExtra("delete", 0)
            sender = PendingIntent.getBroadcast(context, alarmData[x-1].id!!.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
            calendar = Calendar.getInstance()
            date = alarmData[x-1].date

            //실제 달보다 1 작은 숫자로 인식
            val month = date.substring(5, 7).toInt() - 1

            calendar.set(date.substring(0, 4).toInt(), month, date.substring(8).toInt(),0, 0, 0)
            am.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, sender)
        }
    }

    // 알람 삭제할 경우
    fun deleteAlarm(alarmData: MutableList<Schedule>) {
        val intent = Intent(context, Notification::class.java)

        for(x in 1..alarmData.size) {
            intent.putExtra("delete", 1)
            intent.putExtra("id", alarmData[x-1].id)

            sender = PendingIntent.getBroadcast(context, alarmData[x-1].id!!.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
            am.set(AlarmManager.RTC_WAKEUP, 0, sender)
        }
    }
}