package com.example.food_project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.food_project.data.Restaurant
import com.example.food_project.ui.components.CategoryFilters
import com.example.food_project.ui.components.RecipeCard
import com.example.food_project.ui.components.RecipeSearchBar
import com.example.food_project.viewmodel.RestaurantViewModel

@Composable
fun HomeScreen(viewModel: RestaurantViewModel) {

    var selectedRestaurant by remember { mutableStateOf<Restaurant?>(null) }

    Scaffold(containerColor = Color.Transparent) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            RecipeSearchBar(
                searchText = viewModel.searchText,
                onSearchChange = { viewModel.updateSearch(it) }
            )

            CategoryFilters(
                categories = viewModel.categories,
                selectedCategory = viewModel.selectedCategory,
                onCategorySelected = { viewModel.selectCategory(it) }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(viewModel.filteredRestaurants) { resto ->
                    RecipeCard(
                        resto = resto,
                        onClick = { selectedRestaurant = resto }
                    )
                }
            }
        }
    }

    selectedRestaurant?.let { resto ->
        RecipeDetailScreen(resto) {
        }
    }
}