package com.wilsonhernandez.credibanco.data.repository

import com.wilsonhernandez.credibanco.data.database.dao.CrediBancoDao
import com.wilsonhernandez.credibanco.data.database.entities.TransactionsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DatabaseRepository @Inject constructor(val dao: CrediBancoDao) {

     suspend fun getListTransactions() : List<TransactionsEntity> {
        return  withContext(Dispatchers.IO){
            dao.getTransaction()
        }
    }
    fun getListTransactionsObserver() : Flow<List<TransactionsEntity>> {
        return  dao.getTransactionObserver()
    }
    fun getListTransactionsApproved(): Flow<List<TransactionsEntity>> {
        return  dao.getTransactionsWithStateTrue()
    }
    suspend fun cancelTransactions(id: Int,state:Boolean){
        dao.updateEntityState(id,state)
    }

}