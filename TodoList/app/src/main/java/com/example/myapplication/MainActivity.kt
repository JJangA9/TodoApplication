package com.example.myapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Notification.Notification
import com.example.myapplication.RoomDB.Schedule
import com.example.myapplication.RoomDB.ScheduleViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var scheduleViewModel : ScheduleViewModel
    lateinit var adapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //custom action bar 세팅
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.action_bar_layout)
        val title = findViewById<TextView>(R.id.appTitle)
        title.setText("투두")

        //Recycler adapter 설정
        adapter = CustomAdapter({ schedule ->
            //일정 클릭하면 수정화면으로 넘어가기
            val modifyIntent = Intent(this, ScheduleAddActivity::class.java)
            modifyIntent.putExtra("id", schedule.id)
            modifyIntent.putExtra("schedule", schedule.schedule)
            modifyIntent.putExtra("date", schedule.date)
            modifyIntent.putExtra("iconIndex", schedule.iconIndex)
            startActivity(modifyIntent)

        }, {schedule ->
            //deleteDialog(schedule)
        })


        scheduleViewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        scheduleViewModel.getAll().observe(this, Observer<List<Schedule>>{ schedule ->
            //Update UI
            adapter.setSchedule(schedule!!)
        })

        //LayoutManager로 RecyclerView 연결
        val lm = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = lm
        recyclerView.setHasFixedSize(true)

        val fab = findViewById<FloatingActionButton>(R.id.fab_btn)
        //일정 추가 floating button 클릭
        fab.setOnClickListener{
            startActivity(Intent(this@MainActivity, ScheduleAddActivity::class.java))
        }

        //swipe or swap할 경우
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                adapter.swapItems(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //일정 삭제 여부 dialog 호출
                deleteDialog(adapter.getScheduleAt(viewHolder.adapterPosition))
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun deleteDialog(schedule: Schedule) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("삭제하시겠습니까?")
                .setNegativeButton("NO") {_, _ ->
                    //삭제를 취소하면 swipe 취소
                    scheduleViewModel.getAll().observe(this, Observer<List<Schedule>>{ schedule ->
                        //Update UI
                        adapter.setSchedule(schedule!!)
                    })
                }
                .setPositiveButton("YES") {_, _ ->
                    scheduleViewModel.delete(schedule)
                }
        builder.show()
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.settingBtn -> {
                startActivity(Intent(this@MainActivity, SettingActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

}