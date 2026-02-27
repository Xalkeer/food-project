package com.example.food_project.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Classe "enveloppe" pour le JSON racine
@Serializable
data class CategoryResponse(
    @SerialName("categories") val categories: List<CategoryDTO>
)

// Classe représentant un élément de la liste
@Serializable
data class CategoryDTO(
    @SerialName("idCategory") val id: String,
    @SerialName("strCategory") val name: String,
    @SerialName("strCategoryThumb") val imageUrl: String
)