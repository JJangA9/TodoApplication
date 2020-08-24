package com.todo.myapplication.RoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Schedule::class], version = 1)
abstract class ScheduleDB: RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        private var INSTANCE: ScheduleDB? = null

        fun getInstance(context: Context): ScheduleDB? {
            if(INSTANCE == null) {
                synchronized(ScheduleDB::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            ScheduleDB::class.java, "schedule")
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE
        }
    }
}