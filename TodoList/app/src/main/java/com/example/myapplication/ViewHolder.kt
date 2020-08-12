package com.example.myapplication

import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.RoomDB.Schedule
import kotlinx.android.synthetic.main.adapter_iem_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var date = itemView.findViewById<TextView>(R.id.date)
    var period = itemView.findViewById<TextView>(R.id.period)
    var toDo = itemView.findViewById<TextView>(R.id.toDo)
    var iconImg = itemView.findViewById<ImageView>(R.id.scheduleImg)

//    init {
//        itemView.setOnTouchListener {v, event->
//            if(event.action == MotionEvent.ACTION_DOWN) {
//                listener.onStartDrag(this)
//            }
//            false
//        }
//    }

    fun bind(schedule: Schedule) {
        date?.text = schedule.date
        val selectedDate: String
        //기한이 지난 일정은 +로 표시
        if(getTodayDate(schedule.date) < 0) {selectedDate = "D + " + Math.abs(getTodayDate(schedule.date))}
        else {selectedDate = "D - " + getTodayDate(schedule.date)}

        period?.text = selectedDate
        toDo?.text = schedule.schedule

        if(schedule.iconIndex == 0 || schedule.iconIndex == 2) {iconImg.setImageResource(R.drawable.heart)}
        else if(schedule.iconIndex == 3) {iconImg.scheduleImg.setImageResource(R.drawable.travel)}
        else if(schedule.iconIndex == 4) {iconImg.scheduleImg.setImageResource(R.drawable.conference)}
        else if(schedule.iconIndex == 5) {iconImg.scheduleImg.setImageResource(R.drawable.dinner)}
        else if(schedule.iconIndex == 6) {iconImg.scheduleImg.setImageResource(R.drawable.book)}
    }

    //오늘 날짜 가져오기
    private fun getTodayDate(selected: String?):Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
        val currentDate = sdf.format(Date())

        val startDate = sdf.parse(currentDate)
        val endDate = sdf.parse(selected)
        val diff = endDate.time - startDate.time
        return diff / (24 * 60 * 60 * 1000)
    }
}