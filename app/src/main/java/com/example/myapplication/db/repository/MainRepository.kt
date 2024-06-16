package com.example.myapplication.db.repository

import com.example.myapplication.db.dao.ShuffledListStateDao
import com.example.myapplication.db.entity.ShuffledListState
import javax.inject.Inject

class MainRepository @Inject constructor (private val mainDao: ShuffledListStateDao) {

//    private val mainDao = AppDatabase.getDatabase(context).shuffledListStateDao()

    suspend fun saveShuffledListState(wordsStack: ArrayDeque<String>) {
        // Save shuffled list state using Room database
        val state = ShuffledListState(shuffledList = wordsStack)
        mainDao.insert(state)
    }

    suspend fun getShuffledListState(): ArrayDeque<String>? {
        // Retrieve shuffled list state from Room database
        val state = mainDao.getState()
        return state?.shuffledList
    }

    suspend fun clearTable() {
        mainDao.clearTable();
    }

    // Other methods related to the main functionality of your app
}