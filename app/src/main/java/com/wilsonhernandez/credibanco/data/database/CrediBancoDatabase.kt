package com.wilsonhernandez.credibanco.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wilsonhernandez.credibanco.data.database.dao.CrediBancoDao
import com.wilsonhernandez.credibanco.data.database.entities.TransactionsEntity

@Database(entities = [TransactionsEntity::class], version = 1)
abstract class CrediBancoDatabase :RoomDatabase(){
    abstract val dao: CrediBancoDao
}