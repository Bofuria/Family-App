package com.example.myapplication.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.db.entity.MealState

@Dao
interface MealDao {
    @Insert
    suspend fun insert(state: MealState)

    @Query("SELECT * FROM meals")
    suspend fun getAllMeals(): List<MealState>?

    @Query("SELECT * FROM meals WHERE id = :id")
    suspend fun getById(id : Long): MealState?

    @Query("UPDATE meals SET name = :name WHERE id = :id")
    suspend fun updateMeal(id: Long, name: String): Int

    @Query("DELETE FROM meals WHERE id = :id")
    suspend fun deleteMeal(id : Long): Int
}