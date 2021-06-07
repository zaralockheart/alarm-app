package com.example.alarmapp

import android.app.Application
import com.example.alarmapp.data.AlarmRepository
import com.example.alarmapp.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * Application level class. Used to initialized required objects.
 *
 * reference:
 * https://developer.android.com/codelabs/android-room-with-a-view-kotlin#12
 */
class AlarmApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { AlarmRepository(database.alarmDao()) }
}