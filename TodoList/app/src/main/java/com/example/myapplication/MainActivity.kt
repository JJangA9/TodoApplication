package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.RoomDB.Schedule
import com.example.myapplication.RoomDB.ScheduleDB
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var customAdapter:CustomAdapter
    private var scheduleDB : ScheduleDB? = null
    private var scheduleList = listOf<Schedule>()
    lateinit var itemTouchHelper:ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scheduleDB = ScheduleDB.getInstance(this)
        customAdapter = CustomAdapter(this, scheduleList)

        //setRecyclerViewItemTouchListener()
        initRecyclerView()

        val fab = findViewById<FloatingActionButton>(R.id.fab_btn)
        //일정 추가 floating button 클릭
        fab.setOnClickListener{
            startActivity(Intent(this@MainActivity, ScheduleAddActivity::class.java))
        }
        

//        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(customAdapter))
//        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    //오늘 날짜 가져오기
    private fun getTodayDate(selected: String):Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
        val currentDate = sdf.format(Date())

        val startDate = sdf.parse(currentDate)
        val endDate = sdf.parse(selected)
        val diff = endDate.time - startDate.time
        return diff / (24 * 60 * 60 * 1000)
    }

//    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
//        itemTouchHelper.startDrag(viewHolder)
//    }

//    private fun setRecyclerViewItemTouchListener() {
//
//        //1
//        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
//            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, viewHolder1: RecyclerView.ViewHolder): Boolean {
//                //2
//                return false
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
//                //3
//                val position = viewHolder.adapterPosition
//                scheduleList.removeAt(position)
//                recyclerView.adapter!!.notifyItemRemoved(position)
//            }
//        }
//
//        //4
//        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
//        itemTouchHelper.attachToRecyclerView(recyclerView)
//    }

//    override fun onItemClickListener(position:Int) {
//        val addRunnable = Runnable {
//            scheduleDB?.scheduleDao()?.deleteItemByIds(scheduleList[position])
//1
//        }
//
//        val addThread = Thread(addRunnable)
//        addThread.start()
//
//        initRecyclerView()
//    }

    fun initRecyclerView() {
        val r = Runnable {
            try {
                scheduleList = scheduleDB?.scheduleDao()?.getAll()!!
                customAdapter = CustomAdapter(this, scheduleList)
                customAdapter.notifyDataSetChanged()

                recyclerView.adapter = customAdapter
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.setHasFixedSize(true)

            } catch (e: Exception) {
                Log.d("tag", "Error - $e")
            }
        }

        val thread = Thread(r)
        thread.start()
    }

    override fun onDestroy() {
        ScheduleDB.destroyInstance()
        scheduleDB = null
        super.onDestroy()
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