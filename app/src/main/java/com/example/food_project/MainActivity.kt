package com.example.food_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.food_project.data.api.viewModels.CategoryViewModel
import com.example.food_project.data.api.viewModels.RecipesViewModel
import com.example.food_project.ui.screens.HomeScreen
import com.example.food_project.ui.screens.LoadingScreen
import com.example.food_project.ui.theme.FoodprojectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recipesViewModel: RecipesViewModel by viewModels {
            RecipesViewModelFactory(this)
        }

        val categoryViewModel: CategoryViewModel by viewModels {
            CategoryViewModelFactory(this)
        }



        enableEdgeToEdge()
        setContent {
            val isInitialLoading by recipesViewModel.isInitialLoading.collectAsState()

            FoodprojectTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    if (isInitialLoading) {
                        LoadingScreen()
                    } else {
                        HomeScreen(recipesViewModel, categoryViewModel)
                    }
                }
            }
        }
    }
}