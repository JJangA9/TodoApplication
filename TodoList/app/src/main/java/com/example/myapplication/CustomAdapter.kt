package com.example.myapplication

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.RoomDB.Schedule
import kotlinx.android.synthetic.main.adapter_iem_layout.view.*
import java.text.SimpleDateFormat
import java.util.*

class CustomAdapter(var scheduleItemClick: (Schedule) -> Unit, var scheduleItemLongClick: (Schedule) -> Unit): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private var schedule:List<Schedule> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): ViewHolder{
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_iem_layout, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return schedule.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(schedule[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var date = itemView.findViewById<TextView>(R.id.date)
        private var period = itemView.findViewById<TextView>(R.id.period)
        private var toDo = itemView.findViewById<TextView>(R.id.toDo)
        private var iconImg = itemView.findViewById<ImageView>(R.id.scheduleImg)

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

            itemView.setOnClickListener{
                scheduleItemClick(schedule)
            }
            itemView.setOnLongClickListener{
                scheduleItemLongClick(schedule)
                true
            }
        }
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

    fun setSchedule(schedule: List<Schedule>) {
        this.schedule = schedule
        notifyDataSetChanged()
    }

    fun getScheduleAt(position: Int): Schedule{
        return schedule[position]
    }

    fun swapItems(fromPosition: Int, toPosition: Int) {
        if(fromPosition < toPosition) {
            for(i in fromPosition..toPosition - 1) {
                Log.d("down", schedule.toString())
            }
        } else {
            for(i in fromPosition..toPosition + 1) {
                Log.d("up", schedule.toString())
            }

        }
        notifyItemMoved(fromPosition, toPosition)
    }

}