package com.example.food_project

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.food_project.data.api.repository.RecipeRepository
import com.example.food_project.data.api.services.RecipesService
import com.example.food_project.data.api.viewModels.RecipesViewModel
import com.example.food_project.data.database.AppDatabase
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class RecipesViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipesViewModel::class.java)) {
            val httpClient = HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            ignoreUnknownKeys = true
                            isLenient = true
                        }
                    )
                }
            }

            val recipesService = RecipesService(httpClient)

            val db = AppDatabase.getDatabase(context.applicationContext)
            val recipeDao = db.recipeDao()

            val repository = RecipeRepository(recipesService, recipeDao)

            @Suppress("UNCHECKED_CAST")
            return RecipesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}