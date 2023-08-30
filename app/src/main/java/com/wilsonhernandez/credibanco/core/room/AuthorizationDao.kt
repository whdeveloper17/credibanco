package com.wilsonhernandez.credibanco.core.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.wilsonhernandez.credibanco.core.room.entity.AuthorizationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthorizationDao {
    @Insert
    suspend fun insertTransaction(authorizationEntity: AuthorizationEntity)

    @Query("Select * from authorizationEntity")
    fun getAuthorization():Flow<List<AuthorizationEntity>>

    @Query("Select COUNT(*) from authorizationEntity")
    fun  countAuthorization():Flow<String>

    @Query("SELECT * FROM authorizationEntity WHERE state = 1")
    fun getAuthorizedEntities(): Flow<List<AuthorizationEntity>>
    @Delete
    suspend fun deleteAuthorization(authorizationEntity: AuthorizationEntity)

    @Query("UPDATE authorizationEntity SET state = :newState WHERE id = :entityId")
    suspend fun updateEntityState(entityId: Int, newState: Boolean)
}