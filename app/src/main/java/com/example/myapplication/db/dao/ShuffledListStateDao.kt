package com.example.myapplication.db.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.db.entity.ShuffledListState

@Dao
interface ShuffledListStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(state: ShuffledListState)

    @Query("SELECT * FROM shuffled_list LIMIT 1")
    suspend fun getState(): ShuffledListState?

    @Query("DELETE FROM shuffled_list")
    suspend fun clearTable()
}
