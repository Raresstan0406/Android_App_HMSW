package eu.test.healthmonitorsmartwatch

import android.content.Context
import androidx.room.Room
import data.AlarmRepository
import data.ValueRepository
import data.ValuesDataBase

object Graph {
    lateinit var database: ValuesDataBase

    val valuesRepository by lazy {
        ValueRepository(valuesDao= database.valuesDao())
    }
    val alarmsRepository by lazy {
        AlarmRepository(alarmsDao = database.alarmsDao())
    }
    fun provide(context: Context){
        database = Room.databaseBuilder(context, ValuesDataBase::class.java, "Values.db").fallbackToDestructiveMigration().build()
    }
}