package com.example.myapplication.di

import com.example.myapplication.db.dao.HistoryItemDao
import com.example.myapplication.db.dao.MealDao
import com.example.myapplication.db.dao.ShuffledListStateDao
import com.example.myapplication.db.repository.HistoryRepository
import com.example.myapplication.db.repository.MainRepository
import com.example.myapplication.db.repository.MealRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideHistoryRepository(historyItemDao: HistoryItemDao): HistoryRepository {
        return HistoryRepository(historyItemDao)
    }

    @Provides
    fun provideMainRepository(shuffledListStateDao: ShuffledListStateDao): MainRepository {
        return MainRepository(shuffledListStateDao)
    }

    @Provides
    fun provideMealRepository(mealDao: MealDao): MealRepository {
        return MealRepository(mealDao)
    }
}