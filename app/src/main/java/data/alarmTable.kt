package data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Alarms_Table")
data class alarms(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo( name = "D&T")
    var dateTime: String = " ",
    @ColumnInfo( name = "Alarm")
    var alarm: String = " "
)