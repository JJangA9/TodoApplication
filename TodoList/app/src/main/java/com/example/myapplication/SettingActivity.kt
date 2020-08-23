package com.example.myapplication

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.Notification.App
import com.example.myapplication.Notification.AlarmSetting
import com.example.myapplication.RoomDB.Schedule
import com.example.myapplication.RoomDB.ScheduleViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_setting.*
import java.text.SimpleDateFormat
import java.util.*

class SettingActivity : AppCompatActivity() {

    lateinit var notificationManager : NotificationManager
    private lateinit var scheduleViewModel: ScheduleViewModel
    var versionName = BuildConfig.VERSION_NAME

    private lateinit var toggle: Switch
    private var toggleInit: String = "N"
    private lateinit var resetToggle: Switch
    private var resetToggleInit: String = "N"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        scheduleViewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        toggle = findViewById(R.id.toggleBtn)
        resetToggle = findViewById(R.id.resetToggleBtn)

        val appVersion = findViewById<TextView>(R.id.appVersion)
        appVersion.setText("v" + versionName) // 앱 버전 표시
        //애드몹 초기화
        MobileAds.initialize(this) {}
        adView3.loadAd(AdRequest.Builder().build())

        if(App.prefs.notification == "Y") {toggle.isChecked = true} // 알람 받기로 설정해놓은 경우 toggle을 true로 설정
        else {toggle.isChecked = false}
        if(App.prefs.notification_reset == "Y") {resetToggle.isChecked = true} // 알람 받기로 설정해놓은 경우 toggle을 true로 설정
        else {resetToggle.isChecked = false}

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "설정"
        actionbar.setDisplayHomeAsUpEnabled(true)

        toggle.setOnCheckedChangeListener ({ _, isChecked ->
            toggleInit = if (isChecked) "Y" else "N"
            App.prefs.notification = toggleInit
            notiOnOff(toggleInit)
        })

        resetToggle.setOnCheckedChangeListener ({ _, isChecked ->
            resetToggleInit = if (isChecked) "Y" else "N"
            App.prefs.notification_reset = resetToggleInit
            resetOnOff(resetToggleInit)
        })

        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                //Log.d("wow!!!!!!", context.toString())
                scheduleViewModel.getAll().observe(this@SettingActivity, Observer<List<Schedule>> { schedule ->
                    val insertedData = schedule
                    val alarmData: MutableList<Schedule> = mutableListOf()

                    //오늘 날짜 가져오기
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
                    val currentDate = sdf.format(Date())

                    for(x in 1..insertedData.size) {
                        if (insertedData[x - 1].date < currentDate) { // 어제 날짜면
                            alarmData.add(insertedData[x - 1])
                        }
                    }
                    AlarmSetting(applicationContext).deleteAlarm(alarmData) //어제 일정 삭제
                })
            }
        }
        this.registerReceiver(broadcastReceiver, IntentFilter())
    }

    fun notiOnOff(noti: String) {

        if(noti == "Y") { // 사용자가 알림 받기로 바꾼 경우
            scheduleViewModel.getAll().observe(this, Observer<List<Schedule>> { schedule ->
                val insertedData = schedule
                val alarmData: MutableList<Schedule> = mutableListOf()

                //오늘 날짜 가져오기
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
                val currentDate = sdf.format(Date())

                for(x in 1..insertedData.size) {
                    if (insertedData[x - 1].date > currentDate) { //내일부터의 일정 알람 추가
                        alarmData.add(insertedData[x - 1])
                    }
                }

                AlarmSetting(applicationContext).makeAlarm(alarmData)
            })

        } else { // 사용자가 알림 안받기로 바꾼 경우

            scheduleViewModel.getAll().observe(this, Observer<List<Schedule>> { schedule ->
                val insertedData = schedule
                val alarmData: MutableList<Schedule> = mutableListOf()

                //오늘 날짜 가져오기
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
                val currentDate = sdf.format(Date())

                for(x in 1..insertedData.size) {
                    if (insertedData[x - 1].date > currentDate) { //내일부터의 일정 알람 삭제
                        alarmData.add(insertedData[x - 1])
                    }
                }

                AlarmSetting(applicationContext).deleteAlarm(alarmData)
            })
        }
    }

    fun resetOnOff(reset: String) {
        if(reset == "Y") {
            val resetAlarmManager: AlarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, resetReceiver::class.java)
            val sender = PendingIntent.getBroadcast(this, 0, intent, 0)

            val resetCal: Calendar = Calendar.getInstance()
            resetCal.timeInMillis = System.currentTimeMillis()
            resetCal.set(Calendar.HOUR_OF_DAY, 0)
            resetCal.set(Calendar.MINUTE, 0)
            resetCal.set(Calendar.SECOND, 0)

            Log.d("how in setting", resetCal.toString())
            resetAlarmManager.set(AlarmManager.RTC_WAKEUP, 0, sender)

        } else {

        }
    }

    inner class resetReceiver:BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("wow!!!!!!", context.toString())
        }
    }

    //뒤로가기 버튼
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}