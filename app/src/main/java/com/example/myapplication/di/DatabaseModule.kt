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
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Prepopulate the database with initial data
                db.execSQL("INSERT INTO meals (name) VALUES ('Перці')")
                db.execSQL("INSERT INTO meals (name) VALUES ('Голубці')")
                db.execSQL("INSERT INTO meals (name) VALUES ('Пельмені')")
                db.execSQL("INSERT INTO meals (name) VALUES ('\"Наша\" картоша')")
                db.execSQL("INSERT INTO meals (name) VALUES ('Картопляники')")
                db.execSQL("INSERT INTO meals (name) VALUES ('Гречка')")
                db.execSQL("INSERT INTO meals (name) VALUES ('Плов')")
                db.execSQL("INSERT INTO meals (name) VALUES ('Картопля з м''ясом/сосисками')")
                db.execSQL("INSERT INTO meals (name) VALUES ('Риба')")
                db.execSQL("INSERT INTO meals (name) VALUES ('Макарони з м''ясом/сосисками')")
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
