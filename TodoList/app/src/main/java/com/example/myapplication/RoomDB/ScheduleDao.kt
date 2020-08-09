package com.example.myapplication.RoomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.myapplication.RoomDB.Schedule

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedule")
    fun getAll(): List<Schedule>

    @Insert(onConflict = REPLACE)
    fun insert(schedule: Schedule)

    @Query("DELETE from schedule")
    fun deleteAll()
}