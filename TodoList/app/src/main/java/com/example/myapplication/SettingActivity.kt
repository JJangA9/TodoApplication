package com.example.myapplication

import android.app.NotificationManager
import android.content.Context
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        scheduleViewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        toggle = findViewById(R.id.toggleBtn)

        val appVersion = findViewById<TextView>(R.id.appVersion)
        appVersion.setText("v" + versionName) // 앱 버전 표시
        //애드몹 초기화
        MobileAds.initialize(this) {}
        adView3.loadAd(AdRequest.Builder().build())

        if(App.prefs.notification == "Y") {toggle.isChecked = true} // 알람 받기로 설정해놓은 경우 toggle을 true로 설정
        else {toggle.isChecked = false}

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
                        Log.d("alarm yes", insertedData[x-1].id.toString())
                    }
                }

                //Log.d("alarm yes", alarmData.toString())
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
                        Log.d("alarm no", insertedData[x-1].id.toString())
                    }
                }

                AlarmSetting(applicationContext).deleteAlarm(alarmData)
            })
        }
    }
    //뒤로가기 버튼
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}