package com.example.myapplication.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.db.entity.HistoryState
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: HistoryState)

    @Query("SELECT * FROM history_list ORDER BY id DESC LIMIT 10")
    suspend fun getRecentItems(): List<HistoryState>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(historyList: List<HistoryState>)

}
