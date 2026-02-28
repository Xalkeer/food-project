package com.example.food_project.data.api.repository

import com.example.food_project.data.api.services.CategoriesService
import com.example.food_project.data.api.dto.CategoryDTO
import com.example.food_project.data.api.entity.CategoryEntity
import com.example.food_project.data.api.local.CategoriesDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class CategoriesRepository(
    private val apiService: CategoriesService,
    private val dao: CategoriesDAO? = null
) {
    // Sans base de données, on expose un Flow vide pour l'instant
    val categories: Flow<List<CategoryEntity>> =
        dao?.getAllCategories() ?: flowOf(emptyList())

    /**
     * Récupère les catégories depuis l'API distante.
     * Si un DAO est fourni, on pourra plus tard y sauvegarder les données.
     */
    suspend fun refreshCategories(): List<CategoryDTO> {
        return try {
            apiService.getCategories()
        } catch (e: Exception) {
            emptyList()
        }
    }
}