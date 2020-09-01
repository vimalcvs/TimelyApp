package com.crushtech.timelyapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crushtech.timelyapp.R
import com.crushtech.timelyapp.data.entities.Alarms
import com.crushtech.timelyapp.ui.AlarmViewModel
import com.crushtech.timelyapp.ui.CreateAlarmActivity
import kotlinx.android.synthetic.main.alarm_items.view.*

class AlarmAdapter(
    private val alarmViewModel: AlarmViewModel,
    var alarmList: List<Alarms>
) :
    RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alarm_items, parent, false)
        return AlarmViewHolder(view)
    }

    override fun getItemCount(): Int {

        return alarmList.size
    }

    fun getAlarmAt(position: Int): Alarms {
        return alarmList[position]
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val currentAlarm = alarmList[position]
        holder.itemView.time_tv.text = currentAlarm.time
        holder.itemView.days_tv.text = currentAlarm.repeatDays

        //this basically checks the state of the switch
        holder.itemView.isActive.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                currentAlarm.AlarmIsEnabled = true

                CreateAlarmActivity.selectedDays.clear()

                CreateAlarmActivity.startAlarm(currentAlarm.id, holder.itemView.context)
                alarmViewModel.update(currentAlarm)
            } else {
                currentAlarm.AlarmIsEnabled = false
                CreateAlarmActivity.cancelAlarm(currentAlarm.id,holder.itemView.context)
                alarmViewModel.update(currentAlarm)
            }
        }


        holder.itemView.isActive.isChecked = currentAlarm.AlarmIsEnabled
    }


    inner class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
