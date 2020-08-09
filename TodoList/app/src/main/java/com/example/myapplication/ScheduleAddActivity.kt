package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.RoomDB.Schedule
import com.example.myapplication.RoomDB.ScheduleDB
import kotlinx.android.synthetic.main.activity_schedule_add.*
import java.util.*

class ScheduleAddActivity : AppCompatActivity() {

    private lateinit var calendar: Calendar
    var isOpen = false
    var indexOfIcon:Int = 0

    private var scheduleDB : ScheduleDB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_add)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "일정 등록"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        calendar = Calendar.getInstance()
        val thisYear = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        scheduleDB = ScheduleDB.getInstance(this)


        val fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open)
        val fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close)
        val fabRClockwise = AnimationUtils.loadAnimation(this, R.anim.rotate_clockwise)
        val fabRAntiClockwise = AnimationUtils.loadAnimation(this, R.anim.rotate_anticlockwise)

        main_icon.setOnClickListener{
            if(isOpen) {
                second_icon.startAnimation(fabClose)
                third_icon.startAnimation(fabClose)
                fourth_icon.startAnimation(fabClose)
                fifth_icon.startAnimation(fabClose)
                sixth_icon.startAnimation(fabClose)
                main_icon.startAnimation(fabRClockwise)

                isOpen = false
            } else {
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
        //오늘 날짜로 date picker를 초기화
        date_picker.init(
                thisYear,
                month,
                day,
                //날짜가 바뀌면
                DatePicker.OnDateChangedListener{view, year, monthOfYear, dayOfMonth ->

                }

        )

        //저장 버튼 누르면 DB에 일정 저장
        saveBtn.setOnClickListener {
            val schedule: String = scheduleTxt.getText().toString()
            val selectedDate: String = formatDate(date_picker.year, date_picker.month, date_picker.dayOfMonth)

            if(schedule == "") { //일정 내용을 입력 안하면 Toast 띄워줌
                Toast.makeText(this, "일정 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                //새로운 schedule 객체 생성
                val addRunnable = Runnable {
                    val newSche = Schedule()
                    newSche.date = selectedDate
                    newSche.schedule = schedule
                    newSche.iconIndex = indexOfIcon
                    scheduleDB?.scheduleDao()?.insert(newSche)
                }

                val addThread = Thread(addRunnable)
                addThread.start()

                val giveIntent = Intent(this, MainActivity::class.java)
                startActivity(giveIntent)
            }
        }
    }

    //뒤로가기 버튼
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun formatDate(year:Int, m:Int, d:Int):String {
        val mom = m + 1
        var month: String = mom.toString()
        var day: String = d.toString()
        if(month.length == 1) {month = "0" + month}
        if(day.length == 1) {day = "0" + day}

        return year.toString() + "-" + month + "-" + day
    }

    override fun onDestroy() {
        ScheduleDB.destroyInstance()
        super.onDestroy()
    }
}