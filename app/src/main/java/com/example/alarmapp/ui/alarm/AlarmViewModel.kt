package com.example.alarmapp.ui.alarm

import androidx.lifecycle.*
import com.example.alarmapp.data.AlarmEntity
import com.example.alarmapp.data.AlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(private val repository: AlarmRepository) : ViewModel() {

    val allWords: LiveData<List<AlarmEntity>> = repository.allAlarms.asLiveData()

    fun delete(word: AlarmEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(word)
    }

    fun update(word: AlarmEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(word)
    }
}

class AlarmViewModelFactory(private val repository: AlarmRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlarmViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}