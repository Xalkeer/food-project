package com.example.food_project.data.api.services

import com.example.food_project.data.api.dto.RecipesDTO
import com.example.food_project.data.api.dto.RecipesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RecipesService(private val client: HttpClient) {
    private val baseUrl = "https://www.themealdb.com/api/json/v1/1/search.php"

    suspend fun searchRecipes(query: String): List<RecipesDTO> {
        try {
            val response: RecipesResponse = client.get(baseUrl) {
                parameter("s", query)
            }.body()
            val recipesList = response.recipes ?: emptyList()
            recipesList.forEach { dto ->
                println("  📌 Recipe: id=${dto.idMeal}, title=${dto.strMeal}")
            }

            println("🟢 [RecipesService] searchRecipes TERMINÉ - retour de ${recipesList.size} recettes")
            return recipesList

        } catch (e: Exception) {
            println("🔴 [RecipesService] ERREUR dans searchRecipes: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}