package com.example.food_project.data

data class Meal(
    val id: Int,
    val title: String,
    val category: Int,
    val area: String,
    val instructions: String,
    val media: String,
    val ingredients : String,
    val measures : String
)