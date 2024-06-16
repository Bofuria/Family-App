package com.example.myapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.db.util.DataConverter
import com.example.myapplication.db.dao.HistoryItemDao
import com.example.myapplication.db.dao.MealDao
import com.example.myapplication.db.dao.ShuffledListStateDao
import com.example.myapplication.db.entity.HistoryState
import com.example.myapplication.db.entity.MealState
import com.example.myapplication.db.entity.ShuffledListState

@Database(entities = [ShuffledListState::class, HistoryState::class, MealState::class], version = 4, exportSchema = true)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shuffledListStateDao(): ShuffledListStateDao
    abstract fun historyStateDao(): HistoryItemDao
    abstract fun mealDao(): MealDao


//    companion object {
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        fun getDatabase(context: Context): AppDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "app_database"
//                )
//                .fallbackToDestructiveMigration()
//                .build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
}
