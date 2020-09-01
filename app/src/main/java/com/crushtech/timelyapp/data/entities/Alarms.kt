package com.crushtech.timelyapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms_items")
class Alarms(
    @ColumnInfo(name = "alarm_time")
    var time:String,
    @ColumnInfo(name = "repeat_days")
    var repeatDays:String,
    @ColumnInfo(name = "AlarmIsEnabled")
    var AlarmIsEnabled:Boolean=true
        )
{
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
}


