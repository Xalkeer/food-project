package com.example.food_project.data.api.repository

import com.example.food_project.data.api.services.RecipesService
import com.example.food_project.data.api.entity.RecipeEntity
import com.example.food_project.data.api.local.RecipeDao
import kotlinx.coroutines.flow.Flow

class RecipeRepository(
    private val api: RecipesService,
    private val dao: RecipeDao
) {
    val recipes: Flow<List<RecipeEntity>> = dao.getAllRecipes()

    suspend fun refreshRecipes(query: String) {
        try {
            val remoteData = api.searchRecipes(query)

            val entities = remoteData.map { dto ->
                RecipeEntity(
                    id = dto.idMeal,
                    title = dto.strMeal,
                    imageUrl = dto.strMealThumb,
                    description = dto.strInstructions ?: ""
                )
            }
        } catch (e: Exception) {
            // Si l'appel API échoue (pas de réseau), on ne fait rien.
            // L'UI continuera d'afficher les données actuelles de la base (Room).
            throw e
        }
    }
}