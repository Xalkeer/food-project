package com.example.food_project

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.food_project.data.api.repository.RecipeRepository
import com.example.food_project.data.api.local.RecipeDao
import com.example.food_project.data.api.entity.RecipeEntity
import com.example.food_project.data.api.services.RecipesService
import com.example.food_project.data.api.viewModels.RecipesViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.serialization.json.Json

class RecipesViewModelFactory(context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipesViewModel::class.java)) {
            // Créer le HttpClient avec la configuration nécessaire
            val httpClient = HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    })
                }
            }

            // Créer le service API
            val recipesService = RecipesService(httpClient)

            // Créer un DAO mock pour l'instant (tu pourras le connecter plus tard)
            val recipeDao = object : RecipeDao {
                override fun getAllRecipes(): Flow<List<RecipeEntity>> = emptyFlow()
            }

            // Créer le repository
            val repository = RecipeRepository(recipesService, recipeDao)

            @Suppress("UNCHECKED_CAST")
            return RecipesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

