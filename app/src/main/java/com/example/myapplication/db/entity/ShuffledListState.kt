package com.example.myapplication.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.myapplication.db.util.DataConverter

@Entity(tableName = "shuffled_list")
@TypeConverters(DataConverter::class)
data class ShuffledListState(
    @PrimaryKey
    val id : Long = 0,

    @ColumnInfo(name = "item")
    val shuffledList: ArrayDeque<String>

)
