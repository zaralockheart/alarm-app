package com.example.alarmapp.ui.alarmnotification

import android.content.Context.VIBRATOR_SERVICE
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import androidx.fragment.app.Fragment
import com.example.alarmapp.alarm.AlarmUtilities
import com.example.alarmapp.data.AlarmEntity
import java.util.*

/**
 * Base Fragment that will manage ringtone alarm and vibrate.
 */
@Suppress("DEPRECATION")
open class AlarmBaseFragment(val alarm: AlarmEntity) : Fragment() {
    private val ringtone: Ringtone by lazy {RingtoneManager.getRingtone(context, Uri.parse(alarm.sound))}

    private companion object {
        const val vibrationLength = 2000L
        const val snoozeTime = 10
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ringtone.play()

        if (alarm.vibrate) {
            val v = requireContext().getSystemService(VIBRATOR_SERVICE) as Vibrator?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v!!.vibrate(VibrationEffect.createOneShot(vibrationLength, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                v!!.vibrate(vibrationLength)
            }
        }
    }

    protected fun stopRingtoneAndCloseScreen() {
        ringtone.stop()
        activity?.finish()
    }

    protected fun snooze() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, snoozeTime)
        // Since alarm id is based on the time set in long. Use new alarm id with addition of the snooze time.
        AlarmUtilities.setAlarm(requireContext(), alarm.uid + snoozeTime, calendar)
        stopRingtoneAndCloseScreen()
    }
}