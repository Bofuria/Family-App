package com.example.myapplication.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.db.entity.HistoryState
import com.example.myapplication.db.entity.MealState
import com.example.myapplication.db.repository.HistoryRepository
import com.example.myapplication.db.repository.MainRepository
import com.example.myapplication.db.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    @ApplicationContext
    private val context: Context,
    private val mainRepository: MainRepository,
    private val mealRepository: MealRepository,
    private val historyRepository: HistoryRepository) : ViewModel() {

    private val _currentDish = MutableLiveData<String>()
    val currentDish: LiveData<String> = _currentDish

    private var mealsStack = mutableListOf<MealState>()

    private var arrayForIteration = ArrayDeque<String>()

    fun shuffleList() {
        arrayForIteration.clear()
        arrayForIteration.addAll(mealsStack.map { it.name })
        arrayForIteration.shuffle()
        _currentDish.value = arrayForIteration.first()
    }

    fun getNextWord() {
        if (arrayForIteration.isNotEmpty()) {
            arrayForIteration.removeFirst()
            _currentDish.value =
                if (arrayForIteration.isNotEmpty()) arrayForIteration.first()
                else context.getString(R.string.empty_words_list)
        } else {
            _currentDish.value = context.getString(R.string.empty_words_list)
        }
    }

    fun saveWordToHistory() {
        if (arrayForIteration.isNotEmpty()) {
            val currentWord = arrayForIteration.removeFirst()
            viewModelScope.launch(Dispatchers.IO) {
                val currentDate = Date() // Assuming this is the current date
                val historyWithDate = HistoryState(item = currentWord, dateAdded = currentDate)
                historyRepository.saveHistoryItem(historyWithDate)
            }
            _currentDish.value =
                if (arrayForIteration.isNotEmpty()) arrayForIteration.first() else context.getString(R.string.empty_words_list)
        } else {
            _currentDish.value = context.getString(R.string.empty_words_list)
        }
    }

    fun saveList() {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.saveShuffledListState(arrayForIteration)
        }
    }

    suspend fun loadListFromHistory() {
        val state = mainRepository.getShuffledListState()
        if (state != null) {
            arrayForIteration.clear()
            arrayForIteration.addAll(state)
            _currentDish.value = if(arrayForIteration.isEmpty()) context.getString(R.string.empty_words_list) else arrayForIteration.first()
        } else {
            arrayForIteration.addAll(mealsStack.map { it.name })
            _currentDish.value = arrayForIteration.first()
        }
    }

    fun loadAllOptionsMenu() {
        viewModelScope.launch(Dispatchers.IO) {
            mealsStack.clear()
            val meals = mealRepository.getAllMeals() ?: emptyList()
            mealsStack.addAll(meals)
        }
    }
}