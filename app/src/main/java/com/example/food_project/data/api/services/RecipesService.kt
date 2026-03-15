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

    suspend fun getRecipeById(id: String): RecipesDTO? {
        try {
            val response: RecipesResponse = client.get("https://www.themealdb.com/api/json/v1/1/lookup.php") {
                parameter("i", id)
            }.body()
            println("Recipe détail : ${response.recipes?.firstOrNull()}")
            return response.recipes?.firstOrNull()
        } catch (e: Exception) {
            println("🔴 [RecipesService] ERREUR dans getRecipeById: ${e.message}")
            e.printStackTrace()
            return null
        }
    }

    suspend fun searchRecipesByCategory(category: String): List<RecipesDTO> {
        try {
            val response: RecipesResponse = client.get("https://www.themealdb.com/api/json/v1/1/search.php?") {
                parameter("s", category)
            }.body()
            println("Recipe add : ${response} ")
            return response.recipes ?: emptyList()
        } catch (e: Exception) {
            println("🔴 [RecipesService] ERREUR dans getRecipesByCategory: ${e.message}")
            e.printStackTrace()
            return emptyList()
        }
    }

    suspend fun searchRecipesByName(name: String): List<RecipesDTO> {
        try {
            val response: RecipesResponse = client.get(baseUrl) {
                parameter("s", name)
            }.body()
            return response.recipes ?: emptyList()
        } catch (e: Exception) {
            println("🔴 [RecipesService] ERREUR dans searchRecipesByName: ${e.message}")
            e.printStackTrace()
            return emptyList()
        }
    }
}