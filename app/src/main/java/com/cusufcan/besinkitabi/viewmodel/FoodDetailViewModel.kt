package com.cusufcan.besinkitabi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cusufcan.besinkitabi.db.FoodDatabase
import com.cusufcan.besinkitabi.model.Food
import kotlinx.coroutines.launch

class FoodDetailViewModel(application: Application) : AndroidViewModel(application) {
    val foodLiveData = MutableLiveData<Food>()

    fun fetchFromRoom(uuid: Int) {
        viewModelScope.launch {
            val dao = FoodDatabase(getApplication()).foodDao()
            val food = dao.getFood(uuid)
            foodLiveData.value = food
        }
    }
}