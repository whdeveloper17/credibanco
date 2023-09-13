package com.wilsonhernandez.credibanco.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.wilsonhernandez.credibanco.data.repository.AuthorizationApiRepository
import com.wilsonhernandez.credibanco.data.network.service.AuthorizationService
import com.wilsonhernandez.credibanco.core.RetrofitHelper
import com.wilsonhernandez.credibanco.data.database.dao.CrediBancoDao
import com.wilsonhernandez.credibanco.data.database.CrediBancoDatabase
import com.wilsonhernandez.credibanco.data.network.service.CancelService
import com.wilsonhernandez.credibanco.data.repository.CancelApiRepository
import com.wilsonhernandez.credibanco.data.repository.DatabaseRepository
import com.wilsonhernandez.credibanco.settings.SettingsUtil
import com.wilsonhernandez.credibanco.settings.SettingsUtilImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CrediBancoModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
    @Provides
    @Singleton
    fun provideDatabase(context: Context): CrediBancoDatabase {
        return Room.databaseBuilder(context, CrediBancoDatabase::class.java,"credi_banco").build()
    }


    @Provides
    @Singleton
    fun provideCrediBancoDao(appDatabase: CrediBancoDatabase): CrediBancoDao {
        return appDatabase.dao
    }

    @Provides
    @Singleton
    fun provideRetrofitHelper(): RetrofitHelper {
        return RetrofitHelper
    }

    @Provides
    @Singleton
    fun provideAuthorizationService(): AuthorizationService {
        return  AuthorizationService()
    }
    @Provides
    @Singleton
    fun provideCancelService(): CancelService {
        return  CancelService()
    }
    @Provides
    @Singleton
    fun provideAuthorizationRepository( api : AuthorizationService): AuthorizationApiRepository {
        return AuthorizationApiRepository(api)
    }

    @Provides
    @Singleton
    fun provideDatabaseRepository(dao: CrediBancoDao):DatabaseRepository{
        return DatabaseRepository(dao)
    }

    @Provides
    @Singleton
    fun provideCancelRepository(api: CancelService):CancelApiRepository{
        return CancelApiRepository(api)
    }

    @Provides
    @Singleton
    fun provideSettingUtil():SettingsUtil{
        return SettingsUtilImpl()
    }
}