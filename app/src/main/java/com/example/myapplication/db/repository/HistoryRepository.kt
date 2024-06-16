package com.example.myapplication.db.repository

import com.example.myapplication.db.dao.HistoryItemDao
import com.example.myapplication.db.entity.HistoryState
import javax.inject.Inject

class HistoryRepository @Inject constructor(private val historyItemDao: HistoryItemDao) {

//    private val historyDao = AppDatabase.getDatabase(context).historyStateDao()

    suspend fun saveHistoryItem(item : HistoryState) {
        historyItemDao.insert(item)
    }

//    @WorkerThread?
    suspend fun loadHistory(): List<HistoryState>? {
        return historyItemDao.getRecentItems()
    }

//    suspend fun saveHistoryList(itemArrayDeque: ArrayDeque<String>) {
//        val list = itemArrayDeque.map { HistoryState(item = it) }
//        historyItemDao.insertAll(list)
//    }
}