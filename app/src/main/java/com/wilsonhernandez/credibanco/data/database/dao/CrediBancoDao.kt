package com.wilsonhernandez.credibanco.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.wilsonhernandez.credibanco.data.database.entities.TransactionsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CrediBancoDao {
    @Insert
    suspend fun insertTransaction(authorizationEntity: TransactionsEntity)


    @Query("Select * from transactions")
    fun getTransactionObserver():Flow<List<TransactionsEntity>>
    @Query("Select * from transactions")
    fun getTransaction():List<TransactionsEntity>

    @Query("Select COUNT(*) from transactions")
    fun  countTransaction():Flow<String>

    @Query("SELECT * FROM transactions WHERE state = 1")
    fun getTransactionsWithStateTrue(): Flow<List<TransactionsEntity>>
    @Delete
    suspend fun deleteTransaction(transactionsEntity: TransactionsEntity)

    @Query("UPDATE transactions SET state = :newState WHERE id = :entityId")
    suspend fun updateEntityState(entityId: Int, newState: Boolean)
}