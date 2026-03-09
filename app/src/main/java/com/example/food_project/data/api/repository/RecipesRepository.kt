package com.example.food_project.data.api.repository

import com.example.food_project.data.api.entity.RecipeEntity
import com.example.food_project.data.api.local.RecipeDao
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

            //dao.insertAll(entities)
            // Lorsque j'aurais la BDD Interne ça permettra de les enrgistrer et de les afficher ensuite
            println("🟢 [Repository] refreshRecipes TERMINÉ - ${entities.size} recettes")
        } catch (e: Exception) {
            println("🔴 [Repository] ERREUR lors du refresh: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}