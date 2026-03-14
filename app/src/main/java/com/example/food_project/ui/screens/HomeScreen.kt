package com.example.food_project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.food_project.data.api.viewModels.CategoryViewModel
import com.example.food_project.data.api.viewModels.RecipesViewModel
import com.example.food_project.ui.components.CategoryFilters
import com.example.food_project.ui.components.RecipeCard
import com.example.food_project.ui.components.RecipeSearchBar

@Composable
fun HomeScreen(recipesViewModel : RecipesViewModel, categoryViewModel: CategoryViewModel) {

    val categories by categoryViewModel.uiState.collectAsState()
    val recipes by recipesViewModel.filteredRecipes.collectAsState()
    val selectedRecipe by recipesViewModel.selectedRecipe.collectAsState()
    val selectedCategory by recipesViewModel.selectedCategory.collectAsState()
    val searchQuery by recipesViewModel.searchQuery.collectAsState()

    Scaffold(containerColor = Color.Transparent) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            RecipeSearchBar(
                searchText = searchQuery,
                onSearchChange = {
                    recipesViewModel.onSearchQueryChange(it)
                    recipesViewModel.searchRecipes(it)}
            )

            CategoryFilters(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    recipesViewModel.searchRecipesByCategory(category)
                }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(recipes) { recipe ->
                    RecipeCard(
                        recipe = recipe,
                        onClick = { recipesViewModel.selectRecipe(recipe) }
                    )
                }
            }
        }
    }

    selectedRecipe?.let { recipe ->
        RecipeDetailScreen(
            recipe,
            onDismiss = {recipesViewModel.clearSelectedRecipe()})
    }
}