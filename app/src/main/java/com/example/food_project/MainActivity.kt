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
import com.example.food_project.ui.screens.HomeScreen
import com.example.food_project.ui.screens.LoadingScreen
import com.example.food_project.ui.theme.FoodprojectTheme
import com.example.food_project.viewmodel.RestaurantViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodprojectTheme {
                val viewModel: RestaurantViewModel = viewModel()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 2. Logique d'affichage selon l'état de chargement
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