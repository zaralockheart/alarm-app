package com.example.alarmapp.data

enum class AlarmMethod(val value: String) {
    Normal("normal"),
    Shake("shake"),
    EasyMath("easyMath")
}

fun String.toAlarmMethod() = hashMapOf(
    "normal" to AlarmMethod.Normal,
    "shake" to AlarmMethod.Shake,
    "easyMath" to AlarmMethod.EasyMath,
)[this]