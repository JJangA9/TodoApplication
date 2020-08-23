package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.RoomDB.ScheduleViewModel

class ResetMidnight: BroadcastReceiver() {

    private lateinit var scheduleViewModel: ScheduleViewModel

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("how!!!!!!", context.toString())
//        scheduleViewModel = ViewModelProvider(this).get(ScheduleViewModel::class.java)
//        scheduleViewModel = ViewModelProvider.
    }
}