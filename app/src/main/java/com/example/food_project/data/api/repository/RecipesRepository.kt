package com.example.food_project.data.api.repository

import com.example.food_project.data.api.entity.RecipeEntity
import com.example.food_project.data.database.RecipeDao
import com.example.food_project.data.api.mappers.toEntities
import com.example.food_project.data.api.services.RecipesService
import kotlinx.coroutines.flow.Flow

class RecipeRepository(
    val api: RecipesService,
    private val dao: RecipeDao
) {
    val recipes: Flow<List<RecipeEntity>> = dao.getAllRecipes()

    suspend fun refreshRecipes(query: String) {
        try {
            val remoteData = api.searchRecipes(query)
            remoteData.forEach { dto ->
                println("  - id=${dto.idMeal}, title=${dto.strMeal}, thumb=${dto.strMealThumb}, category=${dto.strCategory}")
            }
            val entities = remoteData.toEntities()
            entities.forEach { entity ->
                println("  - id=${entity.id}, title=${entity.title}, category=${entity.category}")
            }

            // ✅ Insertion dans Room (BDD interne)
            println("🔵 [Repository] Insertion de ${entities.size} recettes dans Room...")
            dao.insertRecipes(entities)
            println("🟢 [Repository] ${entities.size} recettes insérées dans la BDD interne")

        } catch (e: Exception) {
            println("🔴 [Repository] ERREUR lors du refresh: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}