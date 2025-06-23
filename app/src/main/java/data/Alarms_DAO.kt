package data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class Alarms_DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addAlarms(alarmEntity: alarms)
    @Query("Select * from `Alarms_Table`")
    abstract fun getAllAlarms(): Flow<List<alarms>>
    @Delete
    abstract suspend fun deleteAlarm(alarmEntity: alarms)
    @Query("DELETE FROM `Alarms_Table`")
    abstract suspend fun deleteAllAlarms()
    @Query("Select * from `Alarms_Table` where id=:id")
    abstract fun getAlarmByID(id: Long): Flow<alarms>
}