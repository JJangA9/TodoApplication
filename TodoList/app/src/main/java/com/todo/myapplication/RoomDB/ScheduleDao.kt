package com.todo.myapplication.RoomDB

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedule ORDER BY date ASC")
    fun getAll(): LiveData<List<Schedule>>

    @Query("SELECT * FROM schedule ORDER BY id DESC limit 1")
    fun getLast(): LiveData<List<Schedule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(schedule: Schedule)

    @Delete
    fun delete(schedule: Schedule)

    @Update
    fun update(schedule: Schedule)
}