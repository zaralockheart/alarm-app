package com.example.alarmapp.ui.setalarm

import android.R.color
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.alarmapp.AlarmApplication
import com.example.alarmapp.alarm.AlarmUtilities
import com.example.alarmapp.data.AlarmEntity
import com.example.alarmapp.data.AlarmMethod
import com.example.alarmapp.data.toAlarmMethod
import com.example.alarmapp.databinding.ActivitySetAlarmBinding
import com.example.alarmapp.ui.AlarmOffMethodActivity
import java.util.*


class SetAlarmActivity : AppCompatActivity() {
    private val setAlarmViewModel: SetAlarmViewModel by viewModels {
        SetAlarmViewModelFactory((application as AlarmApplication).repository)
    }

    private lateinit var binding: ActivitySetAlarmBinding

    private var method: AlarmMethod = AlarmMethod.EasyMath
    private var shakeCount: Int = 0
    private val selectedTime: Calendar by lazy { Calendar.getInstance() }
    private var songSelected: String = ""

    private var days = mapOf(
        "AHD" to Calendar.SUNDAY,
        "ISN" to Calendar.MONDAY,
        "SEL" to Calendar.TUESDAY,
        "RAB" to Calendar.WEDNESDAY,
        "KHA" to Calendar.THURSDAY,
        "JUM" to Calendar.FRIDAY,
        "SAB" to Calendar.SATURDAY
    )

    private var repeatDays = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonBack.setOnClickListener { finish() }
        binding.buttonDone.setOnClickListener {
            storeSavedDataAndSetAlarm()
            finish()
        }

        val calendar = Calendar.getInstance(Locale.getDefault())
        binding.time.text = DateFormat.format("HH : mm", calendar).toString()
        binding.time.setOnClickListener {
            showTimePicker()
        }
        ringtoneSelector()
        alarmOffMethodSelection()

        days.forEach {
            val field = resources.getIdentifier(
                it.key.lowercase(),
                "id", packageName
            )
            findViewById<Button>(field).setOnClickListener { v ->
                if (repeatDays.contains(it.value)) {
                    repeatDays.remove(it.value)
                    v.setBackgroundColor(Color.parseColor("#F5F5F5"))
                } else {
                    repeatDays.add(it.value)
                    v.setBackgroundColor(resources.getColor(color.holo_purple))
                }
            }
        }
    }

    private fun showTimePicker() {
        val c = Calendar.getInstance()
        val mHour = c[Calendar.HOUR_OF_DAY]
        val mMinute = c[Calendar.MINUTE]
        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->

                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                selectedTime.set(Calendar.SECOND, 0)
                binding.time.text = DateFormat.format("HH : mm", selectedTime).toString()
            },
            mHour,
            mMinute,
            false
        )
        timePickerDialog.show()
    }

    private fun storeSavedDataAndSetAlarm() {
        val alarmId: Int = selectedTime.timeInMillis.toInt()
        val data = AlarmEntity(
            uid = alarmId,
            minute = selectedTime.get(Calendar.MINUTE),
            hour = selectedTime.get(Calendar.HOUR_OF_DAY),
            enabled = 1,
            ampm = if (selectedTime.get(Calendar.HOUR_OF_DAY) > 12) "PM" else "AM",
            method = method.value,
            sound = songSelected,
            shakeCount = shakeCount,
            vibrate = binding.switch2.isChecked,
            mon = repeatDays.contains(Calendar.MONDAY),
            tue = repeatDays.contains(Calendar.TUESDAY),
            wed = repeatDays.contains(Calendar.WEDNESDAY),
            thu = repeatDays.contains(Calendar.THURSDAY),
            fri = repeatDays.contains(Calendar.FRIDAY),
            sat = repeatDays.contains(Calendar.SATURDAY),
            sun = repeatDays.contains(Calendar.SUNDAY)
        )
        setRepeatAlarm(alarmId)
        setSingleAlarm(alarmId)
        setAlarmViewModel.insert(data)
    }

    private fun ringtoneSelector() {
        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    val data: Intent = result.data!!
                    val uri =
                        data.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                    songSelected = uri.toString()
                }
            }

        binding.buttonSound.setOnClickListener {
            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
            intent.putExtra(
                RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI,
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            )
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone")
            resultLauncher.launch(intent)
        }
    }

    private fun alarmOffMethodSelection() {
        val alarmOffMethodLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    val selectedMethod = result.data?.getStringExtra("method")
                    selectedMethod?.let { methodString ->
                        method = methodString.toAlarmMethod() ?: AlarmMethod.Normal
                    }
                    val selectedShakeCount = result.data?.getIntExtra("shake_count", 0)
                    shakeCount = selectedShakeCount!!
                }
            }

        binding.buttonMethod.setOnClickListener {
            alarmOffMethodLauncher.launch(
                Intent(this, AlarmOffMethodActivity::class.java)
            )
        }
    }

    private fun setRepeatAlarm(id: Int) {
        repeatDays.forEach {
            val c = Calendar.getInstance()
            c.set(Calendar.DAY_OF_WEEK, it)
            c.set(Calendar.HOUR_OF_DAY, selectedTime[Calendar.HOUR_OF_DAY])
            c.set(Calendar.MINUTE, selectedTime[Calendar.MINUTE])
            c.set(Calendar.SECOND, 0)
            AlarmUtilities.setRepeatAlarm(this, id, id + it, c)
        }
    }

    private fun setSingleAlarm(id: Int) {
        if (repeatDays.isNotEmpty()) return
        AlarmUtilities.setAlarm(this, id, selectedTime)
    }
}