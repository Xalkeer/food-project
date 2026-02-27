package com.example.food_project.data.api

import com.example.food_project.data.api.dto.MealDTO
import com.example.food_project.data.api.dto.MealResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class RecipesService(private val client: HttpClient) {
    private val baseUrl = "https://www.themealdb.com/api/json/v1/1/search.php"

    suspend fun searchRecipes(query: String): List<MealDTO> {
        val response: MealResponse = client.get(baseUrl) {
            parameter("s", query)
        }.body()
        return response.meals ?: emptyList()
    }


}