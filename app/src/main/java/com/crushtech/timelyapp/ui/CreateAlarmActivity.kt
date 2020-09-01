package com.crushtech.timelyapp.ui

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import com.crushtech.timelyapp.R
import com.crushtech.timelyapp.fragments.AlarmFragment.Toast.displayFailureToast
import com.crushtech.timelyapp.receiver.AlarmReceiver
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.activity_create_alarm.*
import java.util.*


class CreateAlarmActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    lateinit var AM_PM: String
    override fun onCreate(savedInstanceState: Bundle?) {
        //get sharedprefs of switch(our switch from settings)
        val sharedPrefs = getSharedPreferences("pref", Context.MODE_PRIVATE)
        val darkModeIsActivated = sharedPrefs.getBoolean("DARK MODE", false)
        if (darkModeIsActivated) {
            setTheme(R.style.DarkMode)
        } else {
            setTheme(R.style.WhiteMode)
        }
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fade_in,R.anim.fadeout)
        setContentView(R.layout.activity_create_alarm)

        addDayChips()

        //set title
        title = "Schedule Alarm"

        btn_choose_time.setOnClickListener {
            val calendar=Calendar.getInstance()
            TimePickerDialog(this, { _, hour, min ->
                selectedHour = hour
                selectedMin = min

                var hourString = selectedHour.toString()
                var minString =  selectedMin.toString()
                if ( selectedHour > 12) {
                    hourString = ( selectedHour - 12).toString()
                    AM_PM = "PM"
                } else {
                    AM_PM = "AM"
                }

                if (selectedMin < 10) {
                    minString = "0$selectedMin"
                }
                val formattedTime = "$hourString:$minString $AM_PM"

                timeTV.text=formattedTime
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()


        }

        //our set alarm button from create alarm xml
        btn_set_alarm.setOnClickListener {
            if(TextUtils.isEmpty(timeTV.text)){
                displayFailureToast(this,"please select a time")
                return@setOnClickListener
            }

            sendDataToAlarmFragment()

        }

    }

    private fun sendDataToAlarmFragment() {
        val timeText = timeTV.text.toString()
        val builder = StringBuilder()
        val alarmIsOn = true
        var repeatdays: String? = null

        when (selectedDays.size) {
            0 -> {
                repeatdays = "Alarm"
            }
            1 -> selectedDays.forEachIndexed { _, day ->
                repeatdays = "Alarm, $day"
            }
            in 2..6 -> {
                selectedDays.forEachIndexed { _, days ->
                    val formatted = days.substring(6, 9)
                    builder.append("$formatted ")
                    repeatdays = builder.toString()
                }
            }
            else -> repeatdays = "Every day"
        }

        val intent = Intent()
        intent.putExtra(ALARM_TIME, timeText)
        intent.putExtra(ALARM_REPEAT_DAYS, repeatdays)
        intent.putExtra(ALARM_IsON, alarmIsOn)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }


    companion object {
        var selectedDays = mutableListOf<String>()
        private var selectedHour = 0
        private var selectedMin = 0

        fun startAlarm(alarmId:Int,context: Context) {
            selectedDays.forEachIndexed { _, day ->
                /**
                 * //Sunday's value is 1 and so index + 1
                 * @see Calendar.SUNDAY
                 */
                val indexOfDay = days.indexOf(day) + 1


                val calendar = Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_WEEK, indexOfDay)
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMin)
                }
                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.DATE, 7)
                }

                val intent = Intent(context, AlarmReceiver::class.java)
                val pendingIntent =
                    PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    7 * 24 * 60 * 60 * 1000,
                    pendingIntent
                )
            }

            if (selectedDays.isEmpty()) {
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMin)
                }
                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.DATE, 1)
                }

                val intent = Intent(context, AlarmReceiver::class.java)
                val pendingIntent =
                    PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }

        }

        //cancel alarm function
        fun cancelAlarm(id: Int, context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent =
                PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.cancel(pendingIntent)
        }

        const val ALARM_TIME = "ALARM_TIME"
        const val ALARM_IsON = "ALARM_IsON"
        const val ALARM_REPEAT_DAYS = "ALARM_REPEAT_DAYS"

        //list of repeat days
        private val days by lazy {
            listOf(
                "Every Sunday",
                "Every Monday",
                "Every Tuesday",
                "Every Wednesday",
                "Every Thursday",
                "Every Friday",
                "Every Saturday"
            )
        }
    }



    private fun addDayChips() {
        days.forEach { day ->
            cg_days_chips.addChip {
                text = day
                tag = day
                isCheckable
                isClickable
                setOnCheckedChangeListener(this@CreateAlarmActivity)
            }
        }
    }

    private fun ChipGroup.addChip(chipInitializer: Chip.() -> Unit) {
        val dayChip =
            layoutInflater.inflate(R.layout.layout_day_chip, null).findViewById<Chip>(R.id.chip_day)
        dayChip.setChipBackgroundColorResource(R.color.chipColor)

        val chip = dayChip.apply {
            chipInitializer(this)
        }
        addView(chip)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            selectedDays.add(buttonView?.tag.toString())
        } else {
            selectedDays.apply {
                removeAt(indexOf(buttonView?.tag.toString()))
            }
        }
    }




}

