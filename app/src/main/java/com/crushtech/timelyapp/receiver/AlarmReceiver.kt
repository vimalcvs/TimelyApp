package com.crushtech.timelyapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Vibrator
import com.crushtech.timelyapp.notification.AlarmNotificationHelper

import java.util.*
@Suppress("DEPRECATION")
class AlarmReceiver :BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
         val alarmNotificationHelper= AlarmNotificationHelper(context)

        vibrator=context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if(vibrator!=null){
           vibrator!!.vibrate(3000)
        }
        alert= RingtoneManager.getDefaultUri (RingtoneManager.TYPE_ALARM)
        taskRingtone=RingtoneManager.getRingtone (context,alert)
        if(taskRingtone!=null){
            taskRingtone!!.play ()
        }

        val notification= alarmNotificationHelper.getNotificationBuilder().build()
        alarmNotificationHelper.getManager().notify(getID(),notification)
    }





  companion  object MyAlarmObjects{
        var taskRingtone: Ringtone? = null
        var alert: Uri? = null
        var vibrator: Vibrator? = null


        fun getID(): Int {
            return (Date().time / 1000L % Int.MAX_VALUE).toInt()
        }
    }


}