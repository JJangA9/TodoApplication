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

    }

//    fun removeItem(view: View) {
//        itemList.removeAt(index)
//        customAdapter.notifyDataSetChanged(index)
//    }

    private fun setValues() {
        itemList!!.add(CardViewItem("D - 10", "2020-08-27", "데이트하기"))
        itemList!!.add(CardViewItem("D - 12", "2020-08-29", "시험"))
        itemList!!.add(CardViewItem("D - 16", "2020-09-02", "헬스장 가기"))
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
