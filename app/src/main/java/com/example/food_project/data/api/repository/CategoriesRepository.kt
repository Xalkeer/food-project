package com.example.food_project.data.api.repository

import com.example.food_project.data.api.services.CategoriesService
import com.example.food_project.data.api.dto.CategoryDTO
import com.example.food_project.data.api.entity.CategoryEntity
import com.example.food_project.data.api.local.CategoriesDAO
import kotlinx.coroutines.flow.Flow

class CategoriesRepository(
    private val apiService: CategoriesService,
    private val dao: CategoriesDAO
) {
    // Flow observé par l'UI
    val categories: Flow<List<CategoryEntity>> = dao.getAllCategories()

    // Rafraîchit les catégories depuis l'API et les stocke en base
    suspend fun refreshCategories(): List<CategoryDTO> {
        return try {
            val remote = apiService.getCategories()
            // Mapper DTO -> Entity
            val entities = remote.map { dto ->
                CategoryEntity(
                    id = dto.id,
                    strCategory = dto.name,
                    strCategoryThumb = dto.imageUrl
                )
            }
            // Sauvegarde en base
            dao.insertAll(entities)
            remote
        } catch (e: Exception) {
            // En cas d'erreur, on renvoie une liste vide
            emptyList()
        }
    }
}