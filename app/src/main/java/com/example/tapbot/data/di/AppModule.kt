package com.example.tapbot.data.di

import android.content.Context
import androidx.room.Room
import com.example.tapbot.data.database.TaskDatabase
import com.example.tapbot.data.sevices.ServiceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideTaskDatabase(@ApplicationContext appContext: Context): TaskDatabase {
        return Room.databaseBuilder(
            appContext, TaskDatabase::class.java, "task_database").build()
    }

    @Singleton
    @Provides
    fun provideServiceManager(@ApplicationContext appContext: Context): ServiceManager {
        return ServiceManager(appContext)
    }
}