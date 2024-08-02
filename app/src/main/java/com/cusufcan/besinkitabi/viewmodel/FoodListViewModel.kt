package com.cusufcan.besinkitabi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cusufcan.besinkitabi.db.FoodDatabase
import com.cusufcan.besinkitabi.model.Food
import com.cusufcan.besinkitabi.service.FoodAPIService
import com.cusufcan.besinkitabi.util.CustomSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FoodListViewModel(application: Application) : AndroidViewModel(application) {
    val foods = MutableLiveData<List<Food>>()
    val foodErrorMessage = MutableLiveData<Boolean>()
    val foodLoading = MutableLiveData<Boolean>()

    private val foodAPIService = FoodAPIService()
    private val customSharedPreferences = CustomSharedPreferences(getApplication())

    private val refreshTime = .1 * 60 * 1000 * 1000 * 1000L

    fun refreshData() {
        val savedTime = customSharedPreferences.getTime()
        if (savedTime != null && savedTime != 0L && System.nanoTime() - savedTime < refreshTime) {
            fetchDataFromRoom()
        } else {
            fetchDataFromInternet()
        }
    }

    fun refreshDataFromInternet() {
        fetchDataFromInternet()
    }

    private fun fetchDataFromRoom() {
        foodLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val foodList = FoodDatabase(getApplication()).foodDao().getAllFood()
            withContext(Dispatchers.Main) {
                showFoods(foodList)
            }
        }
    }

    private fun fetchDataFromInternet() {
        foodLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val foodList = foodAPIService.getData()
            withContext(Dispatchers.Main) {
                foodLoading.value = false
                saveToRoom(foodList)
            }
        }
    }

    private fun showFoods(foodList: List<Food>) {
        foods.value = foodList
        foodErrorMessage.value = false
        foodLoading.value = false
    }

    private fun saveToRoom(foodList: List<Food>) {
        viewModelScope.launch {
            val dao = FoodDatabase(getApplication()).foodDao()
            dao.deleteAll()
            val uuidList = dao.insertAll(*foodList.toTypedArray())
            var i = 0
            while (i < foodList.size) {
                foodList[i].uuid = uuidList[i].toInt()
                i++
            }

            showFoods(foodList)
        }

        customSharedPreferences.saveTime(System.nanoTime())
    }
}