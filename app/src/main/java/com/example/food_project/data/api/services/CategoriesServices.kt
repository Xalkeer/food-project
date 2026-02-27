package com.example.food_project.data.api.services

import com.example.food_project.data.api.dto.CategoryDTO
import com.example.food_project.data.api.dto.CategoryResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class CategoriesService(private val client: HttpClient) {

    private val baseUrl = "https://www.themealdb.com/api/json/v1/1/categories.php"

    suspend fun getCategories(): List<CategoryDTO> {
        return try {
            // On appelle l'API et on convertit directement en objet
            val response: CategoryResponse = client.get(baseUrl).body()

            // On renvoie la liste des catégories
            response.categories
        } catch (e: Exception) {
            // Si une erreur survient (réseau, json mal formé), on renvoie une liste vide
            // plutôt que de faire planter l'application
            emptyList()
        }
    }
}