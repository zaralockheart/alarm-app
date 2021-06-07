package com.example.alarmapp.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.alarmapp.data.AlarmEntity
import java.util.*

/**
 * Utilities class to manage alarm so that the same code doesn't need to be copy pasted everywhere.
 */
object AlarmUtilities {
    fun setAlarm(context: Context, id: Int, calendar: Calendar) {
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        alarmIntent.putExtra(AppIntentService.ALARM_ID, id)
        val pendingIntent = PendingIntent.getBroadcast(
            context, id, alarmIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val now = Calendar.getInstance().timeInMillis
        if (now >= calendar.timeInMillis) {
            calendar.add(Calendar.DAY_OF_WEEK, 1)
        }
        Log.e("setAlarm", calendar.timeInMillis.toString())
        val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    fun setRepeatAlarm(context: Context, id: Int, intentId: Int, calendar: Calendar) {
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        alarmIntent.putExtra(AppIntentService.ALARM_ID, id)
        val pendingIntent = PendingIntent.getBroadcast(
            context, intentId, alarmIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val now = Calendar.getInstance().timeInMillis
        Log.e("setRepeatAlarm1", calendar.timeInMillis.toString())
        if (now >= calendar.timeInMillis) {
            calendar.add(Calendar.DAY_OF_WEEK, 7)
        }
        Log.e("setRepeatAlarm2", calendar.timeInMillis.toString())
        val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY * 7,
            pendingIntent
        )
    }

    fun cancelAllAlarm(context: Context, id: Int) {
        cancelAlarm(context, id)
        cancelRepeatAlarm(context, id)
    }

    private fun cancelAlarm(context: Context, id: Int) {
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, id, alarmIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val alarmManager =
            context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    private fun cancelRepeatAlarm(context: Context, id: Int) {
        mapOf(
            "AHD" to Calendar.SUNDAY,
            "ISN" to Calendar.MONDAY,
            "SEL" to Calendar.TUESDAY,
            "RAB" to Calendar.WEDNESDAY,
            "KHA" to Calendar.THURSDAY,
            "JUM" to Calendar.FRIDAY,
            "SAB" to Calendar.SATURDAY
        ).forEach{
            cancelAlarm(context, id + it.value)
        }
    }
}