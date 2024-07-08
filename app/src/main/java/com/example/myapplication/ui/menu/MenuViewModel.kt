package com.example.myapplication.ui.menu

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.db.entity.HistoryState
import com.example.myapplication.db.entity.MealState
import com.example.myapplication.db.repository.MealRepository
import com.example.myapplication.db.util.ToastType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val mealRepository: MealRepository) : ViewModel() {

    private val _meals = MutableLiveData<List<MealState>>()
    val meals: LiveData<List<MealState>> = _meals

    private val _toastMessage = MutableLiveData<Pair<String, ToastType>>()
    val toastMessage: LiveData<Pair<String, ToastType>> get() = _toastMessage

    init {
        viewModelScope.launch {
            loadMeals()
        }
    }
    private fun loadMeals() {
        viewModelScope.launch {
            mealRepository.getAllMeals().collect { meals ->
                _meals.postValue(meals)
            }
        }
    }

    fun addNewMeal(name : String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newMeal = MealState(name = name)
                mealRepository.saveMeal(newMeal)
                _meals.postValue(_meals.value?.plus(newMeal) ?: listOf(newMeal))
                _toastMessage.postValue(Pair("Meal added successfully", ToastType.SUCCESS))
            } catch (e: Exception) {
                _toastMessage.postValue(Pair("Unexpected error occurred", ToastType.ERROR))
            }
        }
    }

    fun deleteMeal(item : MealState) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mealRepository.deleteMeal(item.id)
                _meals.postValue(_meals.value?.filter { it.id != item.id })
                _toastMessage.postValue(Pair("Meal deleted successfully", ToastType.SUCCESS))
                // TODO: success status notification (with Toast)
            } catch (e: NoSuchElementException) {
                _toastMessage.postValue(Pair("No such element", ToastType.ERROR))
            } catch (e: Exception) {
                _toastMessage.postValue(Pair("Unexpected error occurred", ToastType.ERROR))
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
                _toastMessage.postValue(Pair("Meal updated successfully", ToastType.SUCCESS))
            } catch (e: NoSuchElementException) {
                _toastMessage.postValue(Pair("No such element", ToastType.ERROR))
            } catch (e: Exception) {
                _toastMessage.postValue(Pair("Unexpected error occurred", ToastType.ERROR))
            }
        }
    }
}