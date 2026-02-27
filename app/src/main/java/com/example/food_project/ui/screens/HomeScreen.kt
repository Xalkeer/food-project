package com.example.food_project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.food_project.Restaurant
import com.example.food_project.ui.components.CategoryFilters
import com.example.food_project.ui.components.RecipeCard
import com.example.food_project.ui.components.RecipeDetailScreen
import com.example.food_project.ui.components.RecipeSearchBar

@Composable
fun HomeScreen() {
    val categories = listOf("Tout", "Pizza", "Sushi", "Burger", "Tacos", "Salade")

    var selectedCategory by remember { mutableStateOf("Tout") }
    var searchText by remember { mutableStateOf("") }
    var selectedRestaurant by remember { mutableStateOf<Restaurant?>(null) }

    val restaurants = remember { mutableStateListOf(
        Restaurant("Pizza Hut", "Pizza", "20-30 min", "0.99€", true),
        Restaurant("Sushi Shop", "Sushi", "30-45 min", "2.99€", true),
        Restaurant("Burger King", "Burger", "15-25 min", "Gratuit", false),
        Restaurant("O'Tacos", "Tacos", "20-35 min", "1.50€", true),
        Restaurant("Jour", "Salade", "15-20 min", "2.00€", false),
        Restaurant("Domino's", "Pizza", "25-35 min", "0.99€", true)
    ) }

    val filteredRestaurants = restaurants.filter {
        (selectedCategory == "Tout" || it.category == selectedCategory) &&
                (it.name.contains(searchText, ignoreCase = true))
    }

    Scaffold(containerColor = Color.Transparent) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            RecipeSearchBar(
                searchText = searchText,
                onSearchChange = { searchText = it }
            )

            CategoryFilters(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Restaurants à proximité",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(filteredRestaurants) { resto ->
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
            selectedRestaurant = null
        }
    }
}