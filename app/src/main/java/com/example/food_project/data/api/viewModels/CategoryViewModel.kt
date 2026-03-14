package com.example.food_project.data.api.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_project.data.api.entity.CategoryEntity
import com.example.food_project.data.api.repository.CategoriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: CategoriesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val uiState: StateFlow<List<CategoryEntity>> = _uiState.asStateFlow()

    fun loadCategories() {
        viewModelScope.launch {
            repository.refreshCategories()
            repository.category
                .catch { e ->
                    println("🔴 [ViewModel] Erreur Flow Room: ${e.message}")
                }
                .collect { category ->
                    _uiState.value = category
                }
        }
    }

    init{

        loadCategories()
        println("Category chargé")
    }
}