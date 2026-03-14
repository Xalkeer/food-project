package com.example.food_project.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
    @SerialName("categories") val categories: List<CategoryDTO>
)

@Serializable
data class CategoryDTO(
    @SerialName("idCategory") val idCategory: String,
    @SerialName("strCategory") val strCategory: String,
    @SerialName("strCategoryThumb") val strCategoryThumb: String
)