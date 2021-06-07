package com.example.alarmapp.alarm

import android.app.IntentService
import android.content.Intent
import com.example.alarmapp.ui.alarmnotification.AlarmNotificationActivity

/**
 * Intent service to manage intent and open Alarm Notification Activity.
 *
 * Running on background thread
 *
 * reference: https://developer.android.com/reference/android/app/IntentService
 */
@Suppress("DEPRECATION")
class AppIntentService : IntentService("MyIntentService") {
    override fun onHandleIntent(intent: Intent?) {
        intent?.apply {
            when(intent.action) {
                actionOpenAlarm -> {
                    val targetIntent = Intent(baseContext, AlarmNotificationActivity::class.java)
                    targetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    targetIntent.putExtra(ALARM_ID, intent.getIntExtra("alarmId", 0))
                    application.startActivity(targetIntent)
                }
            }
        }
    }

    companion object {
        const val actionOpenAlarm = "actionOpenAlarm"
        const val ALARM_ID = "alarmId"
    }
}