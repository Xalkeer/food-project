package com.example.food_project

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.food_project.data.api.entity.CategoryEntity

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // 1. Collecte des données (le "magique" qui relie le Repo à l'UI)
            val categoryList by mainViewModel.categories.collectAsState(initial = emptyList())
            Log.d("MainActivity", "Categories: $categoryList") // Debug pour vérifier les données

            // 2. Affichage
            CategoryListScreen(categories = categoryList)
        }
    }
}

@Composable
fun CategoryListScreen(categories: List<CategoryEntity>) {
    LazyRow {
        items(categories) { category ->
            Text(text = category.strCategory) // Remplace par un composant design
        }
    }
}