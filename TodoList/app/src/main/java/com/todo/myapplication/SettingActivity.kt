package com.todo.myapplication

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.todo.myapplication.Notification.AlarmSetting
import com.todo.myapplication.RoomDB.Schedule
import com.todo.myapplication.RoomDB.ScheduleViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.todo.myapplication.Notification.MySharedPreferences
import kotlinx.android.synthetic.main.activity_setting.*
import java.text.SimpleDateFormat
import java.util.*

class SettingActivity : AppCompatActivity() {

    lateinit var notificationManager : NotificationManager
    private lateinit var scheduleViewModel: ScheduleViewModel
    var versionName = BuildConfig.VERSION_NAME

    private lateinit var toggle: Switch
    private var toggleInit: String = "Y"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        MySharedPreferences.init(this)

        scheduleViewModel = ViewModelProvider(this).get(ScheduleViewModel::class.java)
        toggle = findViewById(R.id.toggleBtn)

        val appVersion = findViewById<TextView>(R.id.appVersion)
        appVersion.setText("v" + versionName) // 앱 버전 표시
        //애드몹 초기화
        MobileAds.initialize(this) {}
        adView3.loadAd(AdRequest.Builder().build())

        if(MySharedPreferences.notification == "Y") {toggle.isChecked = true} // 알람 받기로 설정해놓은 경우 toggle을 true로 설정
        else {toggle.isChecked = false}

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "설정"
        actionbar.setDisplayHomeAsUpEnabled(true)

        toggle.setOnCheckedChangeListener ({ _, isChecked ->
            toggleInit = if (isChecked) "Y" else "N"
            MySharedPreferences.notification = toggleInit
            notiOnOff(toggleInit)
        })

        gotoWeb.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yj-development.tistory.com/1"))
            startActivity(intent)
        }
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

    //뒤로가기 버튼
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}