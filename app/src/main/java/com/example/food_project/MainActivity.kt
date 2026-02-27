package com.example.food_project

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.lifecycle.lifecycleScope
import com.example.food_project.data.api.RecipesService
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Initialisation du client Ktor
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }

        // 2. Initialisation du service (Ta classe que nous avons créée)
        val recipeService = RecipesService(client)

        // 3. Test de l'appel API
        lifecycleScope.launch {
            try {
                val recipes = recipeService.searchRecipes("beef")

                if (recipes.isNotEmpty()) {
                    Log.d("API_TEST", "Succès ! Nombre de recettes : ${recipes.size}")
                    Log.d("API_TEST", "Première recette : ${recipes[1].strMeal}")
                } else {
                    Log.d("API_TEST", "Aucune recette trouvée.")
                }
            } catch (e: Exception) {
                Log.e("API_TEST", "Erreur : ${e.message}")
            }
        }

        // 4. On affiche une UI simple
        setContent {
            Text("Food Project - Application en cours...")
        }
    }
}