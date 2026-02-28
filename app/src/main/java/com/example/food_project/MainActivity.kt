package com.example.food_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.food_project.data.api.CategoryViewModel
import com.example.food_project.data.api.entity.CategoryEntity


class MainActivity : ComponentActivity() {

    private val viewModel: CategoryViewModel by viewModels {
        CategoryViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Charger les catégories au démarrage
        viewModel.loadCategories()

        setContent {
            val categories by viewModel.uiState.collectAsState()

            Surface(color = MaterialTheme.colorScheme.background) {
                CategoryListScreen(categories = categories)
            }
        }
    }
}

@Composable
fun CategoryListScreen(categories: List<CategoryEntity>) {
    LazyRow {
        items(categories) { category ->
            Text(text = category.strCategory)
        }
    }
}