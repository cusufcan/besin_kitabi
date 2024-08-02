package com.cusufcan.besinkitabi.util

import android.content.Context
import android.content.SharedPreferences
import kotlin.concurrent.Volatile

class CustomSharedPreferences {
    companion object {
        private const val TIME = "time"
        private var sharedPreferences: SharedPreferences? = null

        @Volatile
        private var instance: CustomSharedPreferences? = null

        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: createCustomSharedPreferences(context).also {
                instance = it
            }
        }

        private fun createCustomSharedPreferences(context: Context): CustomSharedPreferences {
            sharedPreferences =
                androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
            return CustomSharedPreferences()
        }
    }

    fun saveTime(time: Long) {
        sharedPreferences?.edit()?.putLong(TIME, time)?.apply()
    }

    fun getTime() = sharedPreferences?.getLong(TIME, 0)
}