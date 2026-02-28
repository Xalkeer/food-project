package com.example.food_project.data.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_project.data.api.entity.CategoryEntity
import com.example.food_project.data.api.repository.CategoriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: CategoriesRepository) : ViewModel() {

    // État UI interne : categories à afficher dans MainActivity
    private val _uiState = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val uiState: StateFlow<List<CategoryEntity>> = _uiState.asStateFlow()

    fun loadCategories() {
        viewModelScope.launch {
            try {
                val dtos = repository.refreshCategories()

                // Log console pour vérifier le contenu de l'API
                println("Catégories reçues (${dtos.size}):")
                dtos.forEach { dto ->
                    println("- id=${dto.id}, name=${dto.name}, imageUrl=${dto.imageUrl}")
                }

                // Mapper les DTO en entités simples pour l'affichage
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