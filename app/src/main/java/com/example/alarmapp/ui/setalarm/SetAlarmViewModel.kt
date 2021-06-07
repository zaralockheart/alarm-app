package com.example.alarmapp.ui.setalarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.alarmapp.data.AlarmEntity
import com.example.alarmapp.data.AlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SetAlarmViewModel(private val repository: AlarmRepository) : ViewModel() {
    fun insert(word: AlarmEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(word)
    }
}

class SetAlarmViewModelFactory(private val repository: AlarmRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SetAlarmViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SetAlarmViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}