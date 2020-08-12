package com.example.myapplication.RoomDB

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ScheduleViewModel (application: Application) : AndroidViewModel(application) {

    private val repository = ScheduleRepository(application)
    private val schedule = repository.getAll()

    fun getAll() : LiveData<List<Schedule>> {
        return this.schedule
    }

    fun insert(schedule: Schedule){
        repository.insert(schedule)
    }

    fun delete(schedule: Schedule){
        repository.delete(schedule)
    }

    fun update(schedule: Schedule){
        repository.update(schedule)
    }
}