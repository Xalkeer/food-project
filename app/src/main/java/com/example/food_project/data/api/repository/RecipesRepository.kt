package com.example.food_project.data.api.repository

import com.example.food_project.data.api.services.RecipesService
import com.example.food_project.data.api.entity.RecipeEntity
import com.example.food_project.data.api.local.RecipeDao
import kotlinx.coroutines.flow.Flow

class RecipeRepository(
    private val api: RecipesService,
    private val dao: RecipeDao
) {
    // L'UI observe ce Flow : si la base change, l'UI se met à jour
    val recipes: Flow<List<RecipeEntity>> = dao.getAllRecipes()

    // Méthode pour mettre à jour les données (appelée par le ViewModel)
    suspend fun refreshRecipes(query: String) {
        try {
            // 1. Appel API
            val remoteData = api.searchRecipes(query)

            // 2. Conversion (Mapper DTO -> Entity)
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