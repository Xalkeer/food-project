package com.example.food_project.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.food_project.data.Restaurant

class RestaurantViewModel : ViewModel() {

    private val _restaurants = mutableStateListOf(
        Restaurant("Pizza Hut", "Pizza", "20-30 min", "0.99€", true),
        Restaurant("Sushi Shop", "Sushi", "30-45 min", "2.99€", true),
        Restaurant("Burger King", "Burger", "15-25 min", "Gratuit", false),
        Restaurant("O'Tacos", "Tacos", "20-35 min", "1.50€", true),
        Restaurant("Jour", "Salade", "15-20 min", "2.00€", false),
        Restaurant("Domino's", "Pizza", "25-35 min", "0.99€", true)
    )

    var searchText by mutableStateOf("")
    var selectedCategory by mutableStateOf("Tout")
    val categories = listOf("Tout", "Pizza", "Sushi", "Burger", "Tacos", "Salade")

    val filteredRestaurants: List<Restaurant>
        get() = _restaurants.filter { resto ->
            val matchesCategory = selectedCategory == "Tout" || resto.category == selectedCategory
            val matchesSearch = resto.name.contains(searchText, ignoreCase = true)
            matchesCategory && matchesSearch
        }

    fun updateSearch(query: String) {
        searchText = query
    }

    fun selectCategory(category: String) {
        selectedCategory = category
    }

    fun addRestaurant(restaurant: Restaurant) {
        _restaurants.add(0, restaurant)
    }
}