package com.crushtech.timelyapp.ui

import androidx.lifecycle.ViewModel
import com.crushtech.timelyapp.data.entities.Alarms
import com.crushtech.timelyapp.data.repository.AlarmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(private val repos: AlarmRepository) :ViewModel(){

    fun insert(alarm: Alarms)= CoroutineScope(Dispatchers.Main).launch {
        repos.insert(alarm)
    }
    fun update(alarm: Alarms)= CoroutineScope(Dispatchers.Main).launch {
        repos.update(alarm)
    }
    fun delete(alarm: Alarms)= CoroutineScope(Dispatchers.Main).launch {
        repos.delete(alarm)
    }

    //this method is not used nor called yet...
    fun deleteAll()= CoroutineScope(Dispatchers.Main).launch {
        repos.deleteAllAlarms()
    }

    fun getAllAlarms()=repos.getAllAlarms()
}