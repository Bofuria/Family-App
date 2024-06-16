package com.example.myapplication.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "history_list")
data class HistoryState(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val item: String,
    val dateAdded: Date
)