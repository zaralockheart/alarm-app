package com.example.alarmapp.ui.alarmnotification

import android.content.Context.SENSOR_SERVICE
import android.hardware.SensorManager
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.alarmapp.data.AlarmEntity
import com.example.alarmapp.databinding.ActivityShakeAlarmBinding
import com.squareup.seismic.ShakeDetector
import kotlinx.android.synthetic.main.activity_shake_alarm.*


class ShakeAlarmFragment(alarm: AlarmEntity) : AlarmBaseFragment(alarm), ShakeDetector.Listener {

    private var shakeCountDetected = 0

    private var _binding: ActivityShakeAlarmBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ActivityShakeAlarmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sensorManager = activity?.getSystemService(SENSOR_SERVICE) as SensorManager
        val sd = ShakeDetector(this)
        sd.start(sensorManager)
        binding.shakeCounter.text = SpannableStringBuilder(getCount())
    }

    override fun hearShake() {
        binding.shakeCounter.text = SpannableStringBuilder(getCount())
        if (alarm.shakeCount != null) {
            shakeCountDetected++
            if (shakeCountDetected >= alarm.shakeCount) {
                stopRingtoneAndCloseScreen()
            }
        }
    }

    private fun getCount(): String =
        "Shake ${alarm.shakeCount?.minus(shakeCountDetected)} to stop alarm"
}