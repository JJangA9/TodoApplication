package com.example.myapplication.RoomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
class Schedule(@PrimaryKey var id:Long?,
               @ColumnInfo(name = "date") var date: String?,
               @ColumnInfo(name = "schedule") var schedule: String?,
               @ColumnInfo(name = "iconindex") var iconIndex: Int) {
    constructor(): this(null, "", "", 0)
}