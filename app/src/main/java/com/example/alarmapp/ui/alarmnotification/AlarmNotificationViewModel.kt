package com.example.alarmapp.ui.alarmnotification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.alarmapp.data.AlarmRepository

class AlarmNotificationViewModel(private val repository: AlarmRepository) : ViewModel() {
    fun getAlarmById(id: Int) = repository.getAlarmById(id).asLiveData()
}

class AlarmNotificationViewModelFactory(private val repository: AlarmRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmNotificationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlarmNotificationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}