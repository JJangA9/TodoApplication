package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import org.threeten.bp.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    internal var androidRecyclerView: RecyclerView? = null
    internal var itemList: ArrayList<CardViewItem>? = null
    internal var customAdapter:CustomAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        androidRecyclerView = findViewById(R.id.recyclerView) as RecyclerView
        itemList = ArrayList<CardViewItem>()
        setValues()

        customAdapter = CustomAdapter(this, itemList!!)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        androidRecyclerView!!.layoutManager = mLayoutManager
        androidRecyclerView!!.adapter = customAdapter

        val fab = findViewById<FloatingActionButton>(R.id.fab_btn)
        //일정 추가 floating button 클릭
        fab.setOnClickListener{
            startActivity(Intent(this@MainActivity, ScheduleAddActivity::class.java))
            Log.d("haha", itemList.toString())
        }

        if(intent.hasExtra("schedule")) {
            val schedule = intent.getStringExtra("schedule")
            val selected = intent.getStringExtra("selectedDate")
            val index = intent.getIntExtra("indexOfIcon", 0)

            //오늘 날짜 가져오기
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
            val currentDate = sdf.format(Date())

            val startDate = sdf.parse(currentDate)
            val endDate = sdf.parse(selected)
            val diff = endDate.time - startDate.time
            val diffDays = diff / (24 * 60 * 60 * 1000)

            itemList!!.add(CardViewItem("D - " + diffDays, selected, schedule, index))
        }

    }

//    fun removeItem(view: View) {
//        itemList.removeAt(index)
//        customAdapter.notifyDataSetChanged(index)
//    }

    private fun setValues() {
        itemList!!.add(CardViewItem("D - 10", "2020-08-27", "데이트하기",  0))
        itemList!!.add(CardViewItem("D - 12", "2020-08-29", "시험", 6))
        itemList!!.add(CardViewItem("D - 16", "2020-09-02", "헬스장 가기", 5))
    }

//    private fun setRecyclerViewItemTouchListener() {
//        override fun onMove(recyclerView:RecyclerView, viewHolder: CustomViewHolder):Boolean {
//            return false
//        }
//
//        override fun onSwiped(viewHolder:RecyclerView, swipeDir: Int) {
//            val position = viewHolder.adapterPosition
//
//        }
//    }
}
