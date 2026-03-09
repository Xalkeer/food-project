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
import com.example.food_project.data.api.entity.CategoryEntity
import com.example.food_project.data.api.entity.RecipeEntity
import com.example.food_project.data.api.viewModels.CategoryViewModel
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
        categoryViewModel.loadCategories()

        setContent {
            val categories by categoryViewModel.uiState.collectAsState()
            val recipes by recipesViewModel.uiState.collectAsState()
            val isLoading by recipesViewModel.isLoading.collectAsState()
            val errorMessage by recipesViewModel.errorMessage.collectAsState()

            Surface(color = MaterialTheme.colorScheme.background) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Catégories", style = MaterialTheme.typography.headlineMedium)
                    CategoryListScreen(categories = categories)

                    Text(
                        "Recettes",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    Button(onClick = { recipesViewModel.searchRecipes("beef") }) {
                        Text("Charger des recettes")
                    }

                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                    }

                    errorMessage?.let {
                        Text(
                            text = "Erreur: $it",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }

                    RecipesListScreen(recipes = recipes)
                }
            }
        }
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
fun RecipesListScreen(recipes: List<RecipeEntity>) {
    if (recipes.isNotEmpty()) {
        Text(
            text = "Recettes trouvées: ${recipes.size}",
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.bodyLarge
        )

        LazyColumn {
            items(recipes) { recipe ->
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = recipe.title, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "Catégorie: ${recipe.category ?: "N/A"} | Zone: ${recipe.area ?: "N/A"}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}