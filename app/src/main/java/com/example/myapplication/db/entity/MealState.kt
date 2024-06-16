package com.example.myapplication.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealState(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val name: String
)