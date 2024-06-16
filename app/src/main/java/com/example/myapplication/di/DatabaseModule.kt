package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.db.AppDatabase
import com.example.myapplication.db.dao.HistoryItemDao
import com.example.myapplication.db.dao.MealDao
import com.example.myapplication.db.dao.ShuffledListStateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        )
        .fallbackToDestructiveMigration()
        .addCallback(object : RoomDatabase.Callback() {
            override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                super.onDestructiveMigration(db)
                // Prepopulate the database with initial data
                db.execSQL("INSERT INTO meals (name) VALUES ('Apple')")
                db.execSQL("INSERT INTO meals (name) VALUES ('Banana')")
                db.execSQL("INSERT INTO meals (name) VALUES ('Orange')")
                db.execSQL("INSERT INTO meals (name) VALUES ('Mango')")
                db.execSQL("INSERT INTO meals (name) VALUES ('Pineapple')")
                db.execSQL("INSERT INTO meals (name) VALUES ('Pear')")
            }
        })
        .build()
    }

    @Provides
    fun provideHistoryItemDao(appDatabase: AppDatabase): HistoryItemDao {
        return appDatabase.historyStateDao()
    }

    @Provides
    fun provideShuffledListStateDao(appDatabase: AppDatabase): ShuffledListStateDao {
        return appDatabase.shuffledListStateDao()
    }

    @Provides
    fun provideMealDao(appDatabase: AppDatabase): MealDao {
        return appDatabase.mealDao()
    }
}
