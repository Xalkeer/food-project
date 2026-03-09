package com.example.food_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.food_project.data.api.viewModels.CategoryViewModel
import com.example.food_project.data.api.entity.CategoryEntity
import com.example.food_project.data.api.dto.RecipesDTO
import com.example.food_project.data.api.viewModels.RecipesViewModel


class MainActivity : ComponentActivity() {

    private val categoryViewModel: CategoryViewModel by viewModels {
        CategoryViewModelFactory(this)
    }

    private val recipesViewModel: RecipesViewModel by viewModels {
        RecipesViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("🔵 [MainActivity] onCreate START")

        // Charger les catégories au démarrage
        println("🔵 [MainActivity] Appel categoryViewModel.loadCategories()")
        categoryViewModel.loadCategories()

        setContent {
            val categories by categoryViewModel.uiState.collectAsState()
            val recipes by recipesViewModel.uiState.collectAsState()
            val isLoading by recipesViewModel.isLoading.collectAsState()
            val errorMessage by recipesViewModel.errorMessage.collectAsState()

            println("🔵 [MainActivity Composable] Recomposition - categories=${categories.size}, recipes=${recipes.size}, isLoading=$isLoading, error=$errorMessage")

            Surface(color = MaterialTheme.colorScheme.background) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Catégories", style = MaterialTheme.typography.headlineMedium)
                    CategoryListScreen(categories = categories)

                    Text(
                        "Recettes (Test)",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    Button(onClick = {
                        println("🔵 [MainActivity] Bouton cliqué - Lancement de searchRecipes('beef')")
                        recipesViewModel.searchRecipes("beef")
                    }) {
                        Text("Tester API Recettes (beef)")
                    }

                    if (isLoading) {
                        println("🔵 [MainActivity Composable] Affichage du CircularProgressIndicator")
                        CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                    }

                    if (errorMessage != null) {
                        println("🔴 [MainActivity Composable] Affichage erreur: $errorMessage")
                        Text(
                            text = "Erreur: $errorMessage",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }

                    println("🔵 [MainActivity Composable] Affichage RecipesListScreen avec ${recipes.size} recettes")
                    RecipesListScreen(recipes = recipes)
                }
            }
        }

        println("🟢 [MainActivity] onCreate TERMINÉ")
    }
}

@Composable
fun CategoryListScreen(categories: List<CategoryEntity>) {
    LazyRow {
        items(categories) { category ->
            Text(text = category.strCategory, modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun RecipesListScreen(recipes: List<RecipesDTO>) {
    if (recipes.isNotEmpty()) {
        Text(
            "Recettes trouvées: ${recipes.size}",
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        LazyColumn {
            items(recipes) { recipe ->
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = recipe.strMeal, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "Catégorie: ${recipe.strCategory ?: "N/A"} | Zone: ${recipe.strArea ?: "N/A"}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}