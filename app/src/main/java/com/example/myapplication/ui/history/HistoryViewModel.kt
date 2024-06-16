package com.example.myapplication.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.db.entity.HistoryState
import com.example.myapplication.db.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor (private val historyRepository: HistoryRepository) : ViewModel() {

    private val _historyList = MutableLiveData<List<HistoryState>>()
    val historyList: LiveData<List<HistoryState>> = _historyList

    fun loadHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val state = historyRepository.loadHistory() ?: emptyList()
            _historyList.postValue(state)
        }
    }
}