package com.example.food_project.data.api.services

import android.util.Log
import com.example.food_project.data.api.dto.CategoryDTO
import com.example.food_project.data.api.dto.CategoryResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class CategoriesService(private val client: HttpClient) {

    private val baseUrl = "https://www.themealdb.com/api/json/v1/1/categories.php"

    suspend fun getCategories(): List<CategoryDTO> {
        return try {
            Log.d("CategoriesService", "Appel API sur $baseUrl")
            val response: CategoryResponse = client.get(baseUrl).body()
            Log.d("CategoriesService", "Réponse reçue avec ${'$'}{response.categories.size} catégories")
            response.categories
        } catch (e: Exception) {
            Log.e("CategoriesService", "Erreur lors de l'appel API", e)
            emptyList()
        }
    }
}