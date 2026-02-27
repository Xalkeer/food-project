package com.example.food_project.data.api.services

import com.example.food_project.data.api.dto.MealDTO
import com.example.food_project.data.api.dto.MealResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RecipesService(private val client: HttpClient) {
    private val baseUrl = "https://www.themealdb.com/api/json/v1/1/search.php"

    suspend fun searchRecipes(query: String): List<MealDTO> {
        val response: MealResponse = client.get(baseUrl) {
            parameter("s", query)
        }.body()
        return response.meals ?: emptyList()
    }
}