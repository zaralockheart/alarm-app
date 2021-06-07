package com.example.alarmapp.ui.alarmnotification

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.alarmapp.alarm.AlarmUtilities
import com.example.alarmapp.data.AlarmEntity
import com.example.alarmapp.databinding.ActivityEasyMathAlarmBinding
import kotlinx.android.synthetic.main.activity_easy_math_alarm.*
import java.util.*


class EasyMathAlarmFragment(alarm: AlarmEntity) : AlarmBaseFragment(alarm) {
    private var answer: String? = null

    private var _binding: ActivityEasyMathAlarmBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ActivityEasyMathAlarmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        problem.text = SpannableStringBuilder(getMathProblem())

        buttonOk.setOnClickListener {
            val answer = answerEt.text.toString()
            if (answer == this.answer) {
                stopRingtoneAndCloseScreen()
            }
        }

        buttonSnooze.setOnClickListener {
            snooze()
        }
    }

    private fun getMathProblem(): String {
        val random = Random()
        val op = random.nextInt(4)
        var num1: Int
        var num2: Int
        val add1 = 90
        val add2 = 10
        val mult1 = 10
        val mult2 = 3
        return when (op) {
            0 -> {
                num1 = random.nextInt(add1) + add2
                num2 = random.nextInt(add1) + add2
                answer = (num1 + num2).toString()
                "$num1 + $num2 = "
            }
            1 -> {
                num1 = random.nextInt(add1) + add2
                num2 = random.nextInt(add1) + add2
                if (num1 < num2) {
                    val temp: Int = num1
                    num1 = num2
                    num2 = temp
                }
                answer = (num1 - num2).toString()
                "$num1 - $num2 = "
            }
            2 -> {
                num1 = random.nextInt(mult1) + mult2
                num2 = random.nextInt(mult1) + mult2
                answer = (num1 * num2).toString()
                "$num1 * $num2"
            }
            3 -> {
                num1 = random.nextInt(mult1) + mult2
                num2 = random.nextInt(mult1) + mult2
                answer = "%.2f".format(num1.toDouble() / num2)
                if (answer!!.last() == '0') {
                    answer = answer!!.dropLast(1)
                }
                "$num1 / $num2 = "
            }
            else -> ""
        }
    }
}