package com.todo.myapplication.RoomDB

import android.app.Application
import androidx.lifecycle.LiveData

class ScheduleRepository (application: Application){

    private val scheduleDB = ScheduleDB.getInstance(application)!!
    private val scheduleDao: ScheduleDao = scheduleDB.scheduleDao()
    private val schedule: LiveData<List<Schedule>> = scheduleDao.getAll()
    private val scheduleId: LiveData<List<Schedule>> = scheduleDao.getLast()

    fun getAll() : LiveData<List<Schedule>> {
        return schedule
    }

    fun getLast() : LiveData<List<Schedule>> {
        return scheduleId
    }

    fun insert(schedule: Schedule) {
        try{
            val thread = Thread(Runnable {
                scheduleDao.insert(schedule)
            })
            thread.start()
        }catch (e : Exception) {e.printStackTrace()}
    }

    fun delete(schedule: Schedule) {
        try {
            val thread = Thread(Runnable {
                scheduleDao.delete(schedule)
            })
            thread.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun update(schedule: Schedule) {
        try {
            val thread = Thread(Runnable {
                scheduleDao.update(schedule)
            })
            thread.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}