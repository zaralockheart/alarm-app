package com.example.alarmapp.ui.alarmnotification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.alarmapp.R
import com.example.alarmapp.data.AlarmEntity
import kotlinx.android.synthetic.main.activity_easy_math_alarm.*

class NormalAlarmFragment(alarm: AlarmEntity) : AlarmBaseFragment(alarm) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_normal_alarm, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonOk.setOnClickListener {
            stopRingtoneAndCloseScreen()
        }

        buttonSnooze.setOnClickListener {
            snooze()
        }
    }
}