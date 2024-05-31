package com.example.tapbot.data.di

import com.example.tapbot.data.repository.TasksRepositoryImpl
import com.example.tapbot.domain.repository.TasksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(tasksRepositoryImpl: TasksRepositoryImpl): TasksRepository

}