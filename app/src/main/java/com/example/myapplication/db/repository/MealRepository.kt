package com.example.myapplication.db.repository

import com.example.myapplication.db.dao.MealDao
import com.example.myapplication.db.entity.MealState
import javax.inject.Inject

// TODO: Make all functions work with same parameter signature
class MealRepository @Inject constructor(private val mealDao: MealDao) {

    suspend fun saveMeal(item: MealState) {
        mealDao.insert(item)
    }

    suspend fun getAllMeals(): List<MealState>? {
        return mealDao.getAllMeals()
    }

    suspend fun getById(id: Long): MealState {
        val state = mealDao.getById(id) ?: throw NoSuchElementException("Meal with id $id not found")
        return state;
    }

    suspend fun updateMeal(id: Long, name: String) {
        val state = mealDao.updateMeal(id, name)
        if(state == 0) throw NoSuchElementException("Error updating meal with id $id")
    }

    suspend fun deleteMeal(id: Long) {
        val state = mealDao.deleteMeal(id)
        if(state == 0) throw NoSuchElementException("Error deleting meal with id $id")
    }


}