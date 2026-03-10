package com.example.food_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
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
            val navController = rememberNavController()

            val categories by categoryViewModel.uiState.collectAsState()
            val recipes by recipesViewModel.uiState.collectAsState()
            val isLoading by recipesViewModel.isLoading.collectAsState()
            val errorMessage by recipesViewModel.errorMessage.collectAsState()
            val selectedRecipe by recipesViewModel.selectedRecipe.collectAsState()
            val selectedCategory by recipesViewModel.selectedCategory.collectAsState()
            val searchQuery by recipesViewModel.searchQuery.collectAsState()

            MaterialTheme {
                NavHost(navController = navController, startDestination = "list") {
                    composable("list") {
                        Surface(color = MaterialTheme.colorScheme.background) {
                            Column(modifier = Modifier.padding(16.dp)) {

                                // Barre de recherche
                                RecipeSearchBar(
                                    query = searchQuery,
                                    onQueryChange = { recipesViewModel.onSearchQueryChange(it) }
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text("Catégories", style = MaterialTheme.typography.headlineMedium)
                                CategoryListScreen(
                                    categories = categories,
                                    selectedCategory = selectedCategory,
                                    onCategoryClick = { category ->
                                        recipesViewModel.searchRecipesByCategory(category)
                                    }
                                )

                                Text(
                                    text = if (selectedCategory != null) "Recettes · $selectedCategory" else "Recettes",
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier.padding(top = 16.dp)
                                )

                                if (isLoading) {
                                    CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp))
                                }

                                errorMessage?.let {
                                    Text(
                                        text = "Erreur: $it",
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }

                                RecipesListScreen(
                                    recipes = recipes,
                                    onRecipeClick = { recipe ->
                                        recipesViewModel.selectRecipe(recipe)
                                        navController.navigate("detail")
                                    }
                                )
                            }
                        }
                    }

                    composable("detail") {
                        selectedRecipe?.let { recipe ->
                            RecipeDetailScreen(
                                recipe = recipe,
                                onBack = {
                                    recipesViewModel.clearSelectedRecipe()
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryListScreen(
    categories: List<CategoryEntity>,
    selectedCategory: String?,
    onCategoryClick: (String) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(categories) { category ->
            FilterChip(
                selected = category.strCategory == selectedCategory,
                onClick = { onCategoryClick(category.strCategory) },
                label = { Text(category.strCategory) },
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
fun RecipesListScreen(recipes: List<RecipeEntity>, onRecipeClick: (RecipeEntity) -> Unit) {
    if (recipes.isNotEmpty()) {
        Text(
            text = "Recettes trouvées : ${recipes.size}",
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.bodyLarge
        )

        LazyColumn {
            items(recipes) { recipe ->
                RecipeCard(recipe = recipe, onClick = { onRecipeClick(recipe) })
            }
        }
    }
}

@Composable
fun RecipeCard(recipe: RecipeEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = recipe.imageUrl,
                contentDescription = recipe.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "🍽 ${recipe.category ?: "N/A"}  🌍 ${recipe.area ?: "N/A"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Rechercher une recette...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Rechercher"
            )
        },
        singleLine = true,
        shape = MaterialTheme.shapes.large
    )
}
