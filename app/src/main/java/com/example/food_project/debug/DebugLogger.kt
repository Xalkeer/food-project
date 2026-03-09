package com.example.food_project.debug

import android.util.Log

object DebugLogger {
    private const val TAG = "FoodProject"

    fun d(message: String) {
        Log.d(TAG, message)
        println("DEBUG: $message")
    }

    fun e(message: String, throwable: Throwable? = null) {
        Log.e(TAG, message, throwable)
        println("ERROR: $message")
        throwable?.printStackTrace()
    }

    fun i(message: String) {
        Log.i(TAG, message)
        println("INFO: $message")
    }
}

