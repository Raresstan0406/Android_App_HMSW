package data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [healthValues::class, alarms::class],
    version = 2,
    exportSchema = false
)
abstract class ValuesDataBase: RoomDatabase() {
    abstract fun valuesDao() : Values_DAO
    abstract fun alarmsDao(): Alarms_DAO
}