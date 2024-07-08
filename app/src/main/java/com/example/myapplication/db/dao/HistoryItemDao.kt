package com.example.myapplication.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.db.entity.HistoryState
import com.example.myapplication.db.entity.PartialHistoryState

@Dao
interface HistoryItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: HistoryState)

    @Query("SELECT * FROM history_list ORDER BY dateAdded DESC")
    suspend fun getAllHistory(): List<HistoryState>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(historyList: List<HistoryState>)
    @Delete
    suspend fun deleteItem(item: HistoryState)

    @Update(entity = HistoryState::class)
    suspend fun updateItemDate(partialEntity: PartialHistoryState)

    @Query("SELECT COUNT(*) FROM history_list")
    suspend fun getCount(): Int

    @Query("DELETE FROM history_list WHERE id NOT IN (SELECT id FROM history_list ORDER BY dateAdded DESC LIMIT 10)")
    suspend fun deleteOldestItem()

}
