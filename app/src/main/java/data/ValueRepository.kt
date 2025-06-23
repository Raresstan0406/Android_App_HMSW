package data

import kotlinx.coroutines.flow.Flow

class ValueRepository(private val valuesDao: Values_DAO) {
    suspend fun addValues(values: healthValues){
        valuesDao.addValues(values)
    }
    fun getValues(): Flow<List<healthValues>> = valuesDao.getAllValues()

    fun getValuesById(id: Long): Flow<healthValues>{
        return valuesDao.getValuesByID(id)
    }
    suspend fun deleteAllValues(){
        valuesDao.deleteAllValues()
    }
    suspend fun deleteValues(values: healthValues){
        valuesDao.deleteValues(values)
    }
}