package com.example.alarmapp.ui.alarmnotification

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.alarmapp.AlarmApplication
import com.example.alarmapp.R
import com.example.alarmapp.alarm.AppIntentService
import com.example.alarmapp.data.AlarmMethod
import com.example.alarmapp.data.toAlarmMethod


/**
 * Single Alarm Notification Activity. Will show based on method selected.
 */
class AlarmNotificationActivity : AppCompatActivity() {
    private val alarmNotificationViewModel: AlarmNotificationViewModel by viewModels {
        AlarmNotificationViewModelFactory((application as AlarmApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_notification)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
            Log.e("Alarm","equal or above O_MR1")
        } else {
            Log.e("Alarm","lower O_MR1")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
        val alarmId = intent.getIntExtra(AppIntentService.ALARM_ID, 0)
        alarmNotificationViewModel.getAlarmById(alarmId).observe(this, {
            val newFragment = when (it.method.toAlarmMethod()) {
                AlarmMethod.EasyMath -> EasyMathAlarmFragment(it)
                AlarmMethod.Shake -> ShakeAlarmFragment(it)
                else -> NormalAlarmFragment(it)
            }
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.alarmFragment, newFragment)
            transaction.commit()
        })
    }
}