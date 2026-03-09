package com.example.food_project.data.api.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val imageUrl: String,
    val description: String,
    val category: String? = null,
    val area: String? = null,
    val tags: String? = null,
    val youtube: String? = null,
    val source: String? = null,
    val dateModified: String? = null,
    val ingredients: List<String>? = null,
    val measures: List<String>? = null
)
