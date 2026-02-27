package com.example.food_project.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MealResponse(
    @SerialName("meals") val meals: List<MealDTO>? = null
)

@Serializable
data class MealDTO(
    @SerialName("idMeal") val idMeal: String,
    @SerialName("strMeal") val strMeal: String,
    @SerialName("strMealThumb") val strMealThumb: String,
    @SerialName("strInstructions") val strInstructions: String? = null
)