package com.example.food_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.food_project.data.database.AppDatabase
import com.example.food_project.ui.screens.HomeScreen
import com.example.food_project.ui.screens.LoadingScreen
import com.example.food_project.ui.theme.FoodprojectTheme
import com.example.food_project.viewmodel.MealViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getDatabase(applicationContext)
        enableEdgeToEdge()
        setContent {
            FoodprojectTheme {
                val viewModel: MealViewModel = viewModel()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    if (viewModel.isLoading) {
                        LoadingScreen()
                    } else {
                        HomeScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}