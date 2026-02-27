package com.example.food_project.data.api.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    val strCategory: String,
    val strCategoryThumb: String,
)

