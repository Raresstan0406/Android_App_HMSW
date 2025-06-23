package data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Health_Values_Table")
data class healthValues(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo( name = "D&T")
    var dateTime: String = " ",
    @ColumnInfo( name = "Temperature")
    var temperature: String = " ",
    @ColumnInfo( name = "BPM")
    var BPM: String = " ",
    @ColumnInfo( name = "SpO2")
    var SpO2: String = " "
)
