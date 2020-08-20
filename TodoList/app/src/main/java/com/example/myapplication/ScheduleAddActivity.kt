package com.example.myapplication

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.Notification.AlarmSetting
import com.example.myapplication.Notification.App
import com.example.myapplication.RoomDB.Schedule
import com.example.myapplication.RoomDB.ScheduleViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_schedule_add.*
import java.text.SimpleDateFormat
import java.util.*

class ScheduleAddActivity : AppCompatActivity() {

    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var calendar: Calendar
    var isOpen = false
    var indexOfIcon: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_add)

        scheduleViewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "일정 등록"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)

        //애드몹 초기화
        MobileAds.initialize(this) {}
        adView2.loadAd(AdRequest.Builder().build())

        //오늘 날짜 가져오기
        calendar = Calendar.getInstance()
        val thisYear = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open)
        val fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close)
        val fabRClockwise = AnimationUtils.loadAnimation(this, R.anim.rotate_clockwise)
        val fabRAntiClockwise = AnimationUtils.loadAnimation(this, R.anim.rotate_anticlockwise)

        //일정을 수정할 경우
        if (intent.hasExtra("schedule")) {
            scheduleTxt.setText(intent.getStringExtra("schedule"))
            val selectedDate = intent.getStringExtra("date")
            //저장되어 있는 날짜로 날짜 초기화
            date_picker.init(
                    selectedDate.substring(0, 4).toInt(),
                    selectedDate.substring(5, 7).toInt() - 1,
                    selectedDate.substring(8).toInt(),
                    //날짜가 바뀌면
                    DatePicker.OnDateChangedListener { view, year, monthOfYear, dayOfMonth ->

                    }
            )
            val iconIndex = intent.getIntExtra("iconIndex", 0)
            if(iconIndex == 0 || iconIndex == 2) {main_icon.setImageResource(R.drawable.heart)}
            else if(iconIndex == 3) {main_icon.setImageResource(R.drawable.travel)}
            else if(iconIndex == 4) {main_icon.setImageResource(R.drawable.conference)}
            else if(iconIndex == 5) {main_icon.setImageResource(R.drawable.dinner)}
            else if(iconIndex == 6) {main_icon.setImageResource(R.drawable.book)}

        } else { //일정을 추가할 경우 당일 날짜로 초기화
            date_picker.init(
                    thisYear,
                    month,
                    day,
                    //날짜가 바뀌면
                    DatePicker.OnDateChangedListener { view, year, monthOfYear, dayOfMonth ->

                    }
            )
        }

        main_icon.setOnClickListener {

            if (isOpen) {
                showLayout.setBackgroundResource(R.drawable.border_circle)
                hiddenLayout.visibility = View.GONE
                second_icon.startAnimation(fabClose)
                third_icon.startAnimation(fabClose)
                fourth_icon.startAnimation(fabClose)
                fifth_icon.startAnimation(fabClose)
                sixth_icon.startAnimation(fabClose)
                main_icon.startAnimation(fabRClockwise)

                isOpen = false
            } else {
                showLayout.setBackgroundResource(R.drawable.border_topcircle)
                hiddenLayout.visibility = View.VISIBLE
                second_icon.startAnimation(fabOpen)
                third_icon.startAnimation(fabOpen)
                fourth_icon.startAnimation(fabOpen)
                fifth_icon.startAnimation(fabOpen)
                sixth_icon.startAnimation(fabOpen)
                main_icon.startAnimation(fabRAntiClockwise)

                second_icon.isClickable
                third_icon.isClickable
                fourth_icon.isClickable
                fifth_icon.isClickable

                isOpen = true
            }

            second_icon.setOnClickListener {
                main_icon.setImageDrawable(getDrawable(R.drawable.heart))
                //second_icon.setImageDrawable(getDrawable(R.drawable.))
                indexOfIcon = 2
            }
            third_icon.setOnClickListener {
                main_icon.setImageDrawable(getDrawable(R.drawable.travel))
                indexOfIcon = 3
            }
            fourth_icon.setOnClickListener {
                main_icon.setImageDrawable(getDrawable(R.drawable.conference))
                indexOfIcon = 4
            }
            fifth_icon.setOnClickListener {
                main_icon.setImageDrawable(getDrawable(R.drawable.dinner))
                indexOfIcon = 5
            }
            sixth_icon.setOnClickListener {
                main_icon.setImageDrawable(getDrawable(R.drawable.book))
                indexOfIcon = 6
            }
        }
    }

    private fun formatDate(year: Int, m: Int, d: Int): String {
        val mom = m + 1
        var month: String = mom.toString()
        var day: String = d.toString()
        if (month.length == 1) {
            month = "0" + month
        }
        if (day.length == 1) {
            day = "0" + day
        }

        return year.toString() + "-" + month + "-" + day
    }

    //뒤로가기 버튼
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.schedule_add_menu, menu)
        return true
    }

    //저장 버튼을 누르면 Room DB에 저장
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.storeBtn -> {
                val schedule: String = scheduleTxt.getText().toString()
                val selectedDate: String = formatDate(date_picker.year, date_picker.month, date_picker.dayOfMonth)

                if (schedule == "") { //일정 내용을 입력 안하면 Toast 띄워줌
                    Toast.makeText(this, "일정 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                } else {

                    if (intent.hasExtra("schedule")) {//일정 수정일 경우
                        val newSche = Schedule()
                        newSche.date = selectedDate
                        newSche.schedule = schedule
                        newSche.iconIndex = indexOfIcon
                        newSche.id = intent.getLongExtra("id", -1)
                        scheduleViewModel.update(newSche)

                        if (App.prefs.notification == "Y") {
                            scheduleViewModel.getAll().observe(this, Observer<List<Schedule>> { schedule ->
                                val insertedData = schedule
                                val alarmData: MutableList<Schedule> = mutableListOf()

                                for (x in 1..insertedData.size) {
                                    if (insertedData[x - 1].id == newSche.id) { //오늘보다 나중 일정이면 알람 추가
                                        alarmData.add(insertedData[x - 1])
                                    }
                                }
                                AlarmSetting(applicationContext).makeAlarm(alarmData)
                            })
                        } else {}

                    } else {//일정 새로 추가일 경우
                        //새로운 schedule 객체 생성
                        val newSche = Schedule()
                        newSche.date = selectedDate
                        newSche.schedule = schedule
                        newSche.iconIndex = indexOfIcon

                        scheduleViewModel.insert(newSche)

                        //저장한 일정 알림 만들기
                        scheduleViewModel.getLast().observe(this, Observer<List<Schedule>> { schedule ->
                            val insertedData = schedule
                            val alarmData: MutableList<Schedule> = mutableListOf()

                            //오늘 날짜 가져오기
                            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
                            val currentDate = sdf.format(Date())

                            if (App.prefs.notification == "Y") {
                                for (x in 1..insertedData.size) {
                                    if (insertedData[x - 1].date > currentDate) { //오늘보다 나중 일정이면 알람 추가
                                        alarmData.add(insertedData[x - 1])
                                    }
                                }
                                AlarmSetting(applicationContext).makeAlarm(alarmData)
                            } else {
                                //알람 설정이 N 이면 알림 추가 X
                            }
                        })

                    }
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}