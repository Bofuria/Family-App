package com.example.myapplication.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.db.entity.HistoryState
import com.example.myapplication.db.entity.MealState
import com.example.myapplication.db.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val mealRepository: MealRepository) : ViewModel() {

    private val _meals = MutableLiveData<List<MealState>>()
    val meals: LiveData<List<MealState>> = _meals

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // TODO: убрать клоунские вызовы этой функции в других функциях
    fun loadMeals() {
        viewModelScope.launch(Dispatchers.IO) {
            val state = mealRepository.getAllMeals() ?: emptyList()
            _meals.postValue(state)
        }
    }

    fun addNewMeal(name : String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newMeal = MealState(name = name)
                mealRepository.saveMeal(newMeal)
                _meals.postValue(_meals.value?.plus(newMeal) ?: listOf(newMeal))
            } catch (e: Exception) {
                _error.postValue("Exception: $e")
                println("Exception: $e")
            }
        }
    }

    fun deleteMeal(item : MealState) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mealRepository.deleteMeal(item.id)
                _meals.postValue(_meals.value?.filter { it.id != item.id })
                // TODO: success status notification (with Toast)
            } catch (e: Exception) {
                _error.postValue("Exception: $e")
                println("Exception: $e")
            }
        }
    }

    fun updateMeal(id : Long, newName : String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mealRepository.updateMeal(id, newName)
                val updatedMeals = _meals.value!!.map {
                    if (it.id == id) it.copy(name = newName) else it
                }
                _meals.postValue(updatedMeals)
                // TODO: success status notification (with Toast)
            } catch (e: Exception) {
                _error.postValue("Exception: $e")
                println("Exception: $e")
            }
        }
    }
}