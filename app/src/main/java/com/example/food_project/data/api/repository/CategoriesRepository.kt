package com.example.food_project.data.api.repository

import com.example.food_project.data.api.services.CategoriesService
import com.example.food_project.data.api.dto.CategoryDTO
import com.example.food_project.data.api.entity.CategoryEntity
import com.example.food_project.data.database.dao.CategoryDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class CategoriesRepository(
    private val apiService: CategoriesService,
    private val dao: CategoryDao? = null
) {
    suspend fun refreshCategories(): List<CategoryDTO> {
        return try {
            apiService.getCategories()
        } catch (e: Exception) {
            emptyList()
        }
    }
}