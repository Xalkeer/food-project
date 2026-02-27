package com.example.food_project.data.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_project.data.api.entity.CategoryEntity
import com.example.food_project.data.api.repository.CategoriesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: CategoriesRepository) : ViewModel() {
    // 1. Transformation du Flow en StateFlow pour Jetpack Compose
    // L'UI va observer 'uiState'
    val uiState: StateFlow<List<CategoryEntity>> = repository.categories
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 2. Action pour mettre à jour
    fun loadCategories() {
        viewModelScope.launch {
            try {
                repository.refreshCategories()
            } catch (e: Exception) {
                // Gérer l'erreur si besoin
            }
        }
    }
}