package com.example.myapplication.ui.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.db.entity.HistoryState
import com.example.myapplication.db.entity.MealState
import com.example.myapplication.db.entity.PartialHistoryState
import com.example.myapplication.db.repository.HistoryRepository
import com.example.myapplication.db.repository.MealRepository
import com.example.myapplication.db.util.HistoryDialogFragment
import com.example.myapplication.db.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor (
    private val historyRepository: HistoryRepository,
    private val mealRepository: MealRepository
) : ViewModel() {

    private val _historyList = MutableLiveData<List<HistoryState>>()
    val historyList: LiveData<List<HistoryState>> get() = _historyList

    private val _mealsList = MutableLiveData<List<MealState>>()
    val mealsList: LiveData<List<MealState>> get() = _mealsList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _showDialogEvent = SingleLiveEvent<HistoryDialogFragment?>()
    val showDialogEvent: SingleLiveEvent<HistoryDialogFragment?> get() = _showDialogEvent

    init {
        loadHistory()
        loadMeals()
    }

    fun loadHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val state = historyRepository.loadHistory() ?: emptyList()
            _historyList.postValue(state)
        }
    }

    fun loadMeals() {
        viewModelScope.launch(Dispatchers.IO) {
            val meals = mealRepository.getAllMeals() ?: emptyList()
            _mealsList.postValue(meals)
        }
    }

    fun saveHistoryItem(item: HistoryState) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                historyRepository.saveHistoryItem(item)
                withContext(Dispatchers.Main) {
                    val currentList: List<HistoryState> = _historyList.value ?: emptyList()
                    val updatedList = currentList.plus(item).sortedByDescending { it.dateAdded }

                    // Ensure the list does not exceed the desired size (10 in this example)
                    if (updatedList.size > 10) {
                        val trimmedList = updatedList.take(10)
                        _historyList.postValue(trimmedList)
                    } else {
                        _historyList.postValue(updatedList)
                    }
                }
            } catch (e: Exception) {
                _error.postValue("Error saving history item: $e")
            }
        }
    }

    fun showDialog() {
        val meals = _mealsList.value
        if (meals != null) {
            val dialog = HistoryDialogFragment(meals)
            _showDialogEvent.value = dialog
        } else {
            _error.value = "Meals list is not loaded yet."
        }
    }

    fun deleteHistoryItem(item: HistoryState) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                historyRepository.deleteHistoryItem(item)
                _historyList.postValue(_historyList.value?.filter { it.id != item.id })
                // TODO: success status notification (with Toast)
            } catch (e: Exception) {
                _error.postValue("Exception: $e")
                println("Exception: $e")
            }
        }
    }
}
