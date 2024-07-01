package com.example.myapplication.db.repository

import com.example.myapplication.db.dao.HistoryItemDao
import com.example.myapplication.db.entity.HistoryState
import com.example.myapplication.db.entity.PartialHistoryState
import javax.inject.Inject

class HistoryRepository @Inject constructor(private val historyItemDao: HistoryItemDao) {

    private val maxRows = 10

    suspend fun saveHistoryItem(item : HistoryState) {
        val count = historyItemDao.getCount()
        if (count > maxRows) {
            historyItemDao.deleteOldestItem()
        }
        historyItemDao.insert(item)
    }

    suspend fun loadHistory(): List<HistoryState>? {
        return historyItemDao.getAllHistory()
    }

    suspend fun deleteHistoryItem(item : HistoryState) {
        historyItemDao.deleteItem(item)
    }

    suspend fun updateItemDate(partialHistoryState: PartialHistoryState) {
        historyItemDao.updateItemDate(partialHistoryState)
    }

}