package com.example.food_project.data.api.repository

import com.example.food_project.data.api.dto.CategoryDTO
import com.example.food_project.data.api.entity.CategoryEntity
import com.example.food_project.data.api.entity.RecipeEntity
import com.example.food_project.data.api.mappers.toEntities
import com.example.food_project.data.api.services.CategoriesService
import com.example.food_project.data.database.dao.CategoryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CategoriesRepository(
    private val apiService: CategoriesService,
    private val dao: CategoryDao
) {

    val category: Flow<List<CategoryEntity>> = dao.getAll()

    suspend fun refreshCategories() {
        withContext(Dispatchers.IO) {
            try {
                val remoteData = apiService.getCategories()

                var entities = listOf(
                    CategoryEntity(
                        id = "0",
                        strCategory = "All",
                        strCategoryThumb = ""
                    )
                )
                entities += remoteData.toEntities()

                dao.insertCategories(entities)
                println("🟢 [Repository] ${entities.size} recettes insérées dans la BDD interne")

            } catch (e: Exception) {
                println("🔴 [Repository] ERREUR lors du refresh: ${e.message}")
                e.printStackTrace()
                throw e
            }
        }
    }
}