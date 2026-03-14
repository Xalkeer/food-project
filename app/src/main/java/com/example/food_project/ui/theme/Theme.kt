package com.example.food_project.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    background = BackgroundGray,
    surface = Color.White,
    onSurface = Color.Black,
    onSurfaceVariant = TextGray
)

@Composable
fun FoodprojectTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}