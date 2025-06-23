package data

import kotlinx.coroutines.flow.Flow

class AlarmRepository(private val alarmsDao: Alarms_DAO) {
    suspend fun addAlarms(values: alarms){
        alarmsDao.addAlarms(values)
    }
    fun getAlarms(): Flow<List<alarms>> = alarmsDao.getAllAlarms()

    fun getAlarmsById(id: Long): Flow<alarms>{
        return alarmsDao.getAlarmByID(id)
    }

    suspend fun deleteAllAlarms(){
        alarmsDao.deleteAllAlarms()
    }

    suspend fun deleteAlarms(values: alarms){
        alarmsDao.deleteAlarm(values)
    }
}