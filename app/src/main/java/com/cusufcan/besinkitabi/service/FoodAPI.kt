package com.cusufcan.besinkitabi.service

import com.cusufcan.besinkitabi.model.Food
import retrofit2.http.GET

interface FoodAPI {
    // URL -> https://raw.githubusercontent.com/atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json
    // BASE URL -> https://raw.githubusercontent.com/
    // ENDPOINT -> atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json

    @GET("atilsamancioglu/BTK20-JSONVeriSeti/master/besinler.json")
    suspend fun getFood(): List<Food>
}