package com.example.myapplication

import android.content.Context
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

class CustomAdapter(internal var context: Context, internal var itemList:List<Schedule>): RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): ViewHolder{
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_iem_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.itemView
        val childView = cardView.childView
        holder?.bind(itemList[position])

        //카드뷰 클릭하면 수정,삭제 버튼 나타나게 하기
        cardView.setOnClickListener{
            if(childView.visibility == View.GONE) {
                //TransitionManager.beginDelayedTransition(holder.cardView, AutoTransition())
                childView.visibility = View.VISIBLE
            } else {
                //TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                childView.visibility = View.GONE
            }
        }

        //수정 버튼을 클릭하면 일정 수정
        cardView.modifyBtn.setOnClickListener {

        }

        //삭제 버튼을 클릭하면 일정 삭제
        cardView.deleteBtn.setOnClickListener {
            //removeItem(position);
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    //일정 제거
//    fun removeItem(position: Int) {
//        itemList.removeAt(position)
//        notifyDataSetChanged()
//    }

    //일정 추가
//    fun addItem(item: CardViewItem){
//        itemList.add(item)
//        notifyItemInserted(itemList.lastIndex)
//    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date = itemView.findViewById<TextView>(R.id.date)
        var period = itemView.findViewById<TextView>(R.id.period)
        var toDo = itemView.findViewById<TextView>(R.id.toDo)
        var iconImg = itemView.findViewById<ImageView>(R.id.scheduleImg)

        fun bind(schedule: Schedule) {
                date?.text = schedule.date
                val selectedDate = "D - " + getTodayDate(schedule.date)

                period?.text = selectedDate
                toDo?.text = schedule.schedule

                if(schedule.iconIndex == 0 || schedule.iconIndex == 2) {iconImg.setImageResource(R.drawable.heart)}
                else if(schedule.iconIndex == 3) {iconImg.scheduleImg.setImageResource(R.drawable.travel)}
                else if(schedule.iconIndex == 4) {iconImg.scheduleImg.setImageResource(R.drawable.conference)}
                else if(schedule.iconIndex == 5) {iconImg.scheduleImg.setImageResource(R.drawable.dinner)}
                else if(schedule.iconIndex == 6) {iconImg.scheduleImg.setImageResource(R.drawable.book)}
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
}