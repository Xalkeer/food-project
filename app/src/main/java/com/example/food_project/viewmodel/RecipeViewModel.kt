package com.example.food_project.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_project.data.Meal
import com.example.food_project.data.database.Category
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MealViewModel : ViewModel() {
    var isLoading by mutableStateOf(true)
        private set

    private val _meals = mutableStateListOf<Meal>()

    var searchText by mutableStateOf("")
    var selectedCategory  by mutableStateOf(Category(0, "Tout", ""))
    var categories = mutableStateListOf<Category>()


    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            delay(2000)

            // #TODO : C
            categories.addAll(
                listOf(
                    Category(0, "Tout", ""),
                    Category(1, "Pizza", ""),
                    Category(2, "Sushi", "")
                )
            )

            _meals.addAll(
                listOf(
                    Meal(1, "Pizza", 1, "0.99€", "Pizza", "Pizza","Pizza","Pizza",),
                    Meal(2, "Sushi", 1, "2.99€", "Pizza", "Pizza","Pizza","Pizza",),
                    Meal(3, "Burger", 1, "Gratuit", "Pizza", "Pizza","Pizza","Pizza",),
                    Meal(4, "Tacos", 2, "1.50€", "Pizza", "Pizza","Pizza","Pizza",),
                    Meal(5, "Salade", 2, "2.00€", "Pizza", "Pizza","Pizza","Pizza",),
                    Meal(6, "Pizza", 2, "0.99€", "Pizza", "Pizza","Pizza","Pizza",)
                )
            )

            isLoading = false
        }
    }

    val filteredRestaurants: List<Meal>
        get() = _meals.filter { resto ->
            val matchesCategory = selectedCategory.title == "Tout" || resto.category == selectedCategory.id
            val matchesSearch = resto.title.contains(searchText, ignoreCase = true)
            matchesCategory && matchesSearch
        }

    fun updateSearch(query: String) {
        searchText = query
    }

    fun selectCategory(category: Category) {
        selectedCategory = category
    }
}