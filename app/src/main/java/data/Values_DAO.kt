package data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class Values_DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addValues(userEntity: healthValues)
    @Query("Select * from `Health_Values_Table`")
    abstract fun getAllValues(): Flow<List<healthValues>>
    @Delete
    abstract suspend fun deleteValues(userEntity: healthValues)
    @Query("DELETE FROM `Health_Values_Table`")
    abstract suspend fun deleteAllValues()
    @Query("Select * from `Health_Values_Table` where id=:id")
    abstract fun getValuesByID(id: Long): Flow<healthValues>
}

//@Update
//abstract suspend fun updateValue(userEntity: personalData)