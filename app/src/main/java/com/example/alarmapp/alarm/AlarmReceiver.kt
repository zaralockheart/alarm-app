package com.example.alarmapp.alarm

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.legacy.content.WakefulBroadcastReceiver

/**
 * This class takes care of creating and managing a partial wake lock. Helping on trigger pending intent set with time.
 *
 * reference:
 * https://developer.android.com/training/scheduling/alarms
 */
@Suppress("DEPRECATION")
class AlarmReceiver : WakefulBroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val service = Intent(context, AppIntentService::class.java)
        service.action = AppIntentService.actionOpenAlarm
        service.putExtra(AppIntentService.ALARM_ID, intent.getIntExtra(AppIntentService.ALARM_ID, 0))
        val comp = ComponentName(context.packageName, AppIntentService::class.java.name)
        ContextCompat.startForegroundService(context,intent.setComponent(comp))
        startWakefulService(context, service)
    }
}