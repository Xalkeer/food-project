package com.example.food_project.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.food_project.data.api.entity.RecipeEntity
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(recipe: RecipeEntity, onDismiss: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Détails", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {

                AsyncImage(
                    model = recipe.imageUrl,
                    contentDescription = recipe.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )

                Column(modifier = Modifier.padding(20.dp)) {

                    Text(
                        text = recipe.title,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        recipe.category?.let { SuggestionChip(onClick = {}, label = { Text(it) }) }
                        recipe.area?.let { SuggestionChip(onClick = {}, label = { Text(it) }) }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                    if (!recipe.ingredients.isNullOrEmpty()) {
                        Text(
                            text = "Ingrédients",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        recipe.ingredients.zip(recipe.measures ?: emptyList()).forEach { (ingredient, measure) ->
                            Text(
                                text = "• $ingredient ($measure)",
                                modifier = Modifier.padding(vertical = 2.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                    }

                    Text(
                        text = "Instructions de préparation",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = recipe.description,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 24.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}