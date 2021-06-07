package com.example.alarmapp.data

import android.support.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

/**
 * Repository to connect to Database Object.
 *
 * reference:
 * https://developer.android.com/codelabs/android-room-with-a-view-kotlin#8
 */
class AlarmRepository(private val alarmDao: AlarmDao) {

    val allAlarms: Flow<List<AlarmEntity>> = alarmDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: AlarmEntity) {
        alarmDao.insertAll(word)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(word: AlarmEntity) {
        alarmDao.delete(word)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(word: AlarmEntity) {
        alarmDao.update(word)
    }

    fun getAlarmById(id: Int): Flow<AlarmEntity> = alarmDao.loadOne(id)
}