package com.crushtech.timelyapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.crushtech.timelyapp.data.entities.Alarms

@Dao
interface AlarmDao {

    // this method  inserts alarm items into room database
    @Insert
    suspend fun insert(alarm: Alarms)

    // this method  update an inserted alarm items into room database
    @Update
    suspend fun update(alarm:Alarms)

    // this method  deletes alarm items into room database
    @Delete
    suspend fun delete(alarm: Alarms)

    // this method  deletes all alarm items from room database
    @Query("DELETE FROM alarms_items")
    suspend fun deleteAllAlarms()

    // this method gets all alarm items from room database
    @Query("SELECT * FROM alarms_items")
    fun getAllAlarms():LiveData<List<Alarms>>
}