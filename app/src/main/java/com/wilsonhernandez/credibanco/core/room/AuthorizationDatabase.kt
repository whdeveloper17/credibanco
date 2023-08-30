package com.wilsonhernandez.credibanco.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wilsonhernandez.credibanco.core.room.entity.AuthorizationEntity

@Database(entities = [AuthorizationEntity::class], version = 1)
abstract class AuthorizationDatabase :RoomDatabase(){
    abstract val dao: AuthorizationDao
}