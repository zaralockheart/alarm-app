package com.example.alarmapp.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * Local database with room
 *
 * reference:
 * https://developer.android.com/training/data-storage/room
 *
 * codelab:
 * https://developer.android.com/codelabs/android-room-with-a-view-kotlin#5
 */
@Entity(tableName = "alarm")
data class AlarmEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "hour") val hour: Int,
    @ColumnInfo(name = "minute") val minute: Int,
    @ColumnInfo(name = "method") val method: String,
    @ColumnInfo(name = "shakeCount") val shakeCount: Int?,
    @ColumnInfo(name = "ampm") val ampm: String,
    @ColumnInfo(name = "sound") val sound: String,
    @ColumnInfo(name = "enabled") val enabled: Int?,
    @ColumnInfo(name = "vibrate") val vibrate: Boolean,
    @ColumnInfo(name = "mon") val mon: Boolean?,
    @ColumnInfo(name = "tue") val tue: Boolean?,
    @ColumnInfo(name = "wed") val wed: Boolean?,
    @ColumnInfo(name = "thu") val thu: Boolean?,
    @ColumnInfo(name = "fri") val fri: Boolean?,
    @ColumnInfo(name = "sat") val sat: Boolean?,
    @ColumnInfo(name = "sun") val sun: Boolean?,
)

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm")
    fun getAll(): Flow<List<AlarmEntity>>

    @Query("SELECT * FROM alarm")
    fun getAllList(): List<AlarmEntity>

    @Query("SELECT * FROM alarm WHERE uid=:id ")
    fun loadOne(id: Int): Flow<AlarmEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(item: AlarmEntity): Int

    @Insert
    fun insertAll(vararg users: AlarmEntity)

    @Delete
    fun delete(user: AlarmEntity)

}

/**
 * Suggested by https://developer.android.com/codelabs/android-room-with-a-view-kotlin#7
 */
@Database(entities = [AlarmEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "alarm_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}