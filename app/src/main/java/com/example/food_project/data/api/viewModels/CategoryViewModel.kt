package com.example.food_project.data.api.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_project.data.api.entity.CategoryEntity
import com.example.food_project.data.api.repository.CategoriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: CategoriesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val uiState: StateFlow<List<CategoryEntity>> = _uiState.asStateFlow()

    fun loadCategories() {
        viewModelScope.launch {
            try {
                val dtos = repository.refreshCategories()

                println("Catégories reçues (${dtos.size}):")
                dtos.forEach { dto ->
                    println("- id=${dto.id}, name=${dto.name}, imageUrl=${dto.imageUrl}")
                }

                _uiState.value = dtos.map { dto ->
                    CategoryEntity(
                        id = dto.id,
                        strCategory = dto.name,
                        strCategoryThumb = dto.imageUrl
                    )
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}