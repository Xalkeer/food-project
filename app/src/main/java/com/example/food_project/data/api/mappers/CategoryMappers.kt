package com.example.food_project.data.api.mappers

import com.example.food_project.data.api.dto.CategoryDTO
import com.example.food_project.data.api.entity.CategoryEntity

// Mapper extensions for Category-related models

fun CategoryDTO.toEntity(): CategoryEntity = CategoryEntity(
    id = this.id,
    strCategory = this.name,
    strCategoryThumb = this.imageUrl
)
fun CategoryEntity.toDTO(): CategoryDTO = CategoryDTO(
    id = this.id,
    name = this.strCategory,
    imageUrl = this.strCategoryThumb
)

fun List<CategoryEntity>.toDTOs(): List<CategoryDTO> = this.map { it.toDTO() }

fun List<CategoryDTO>.toEntities(): List<CategoryEntity> = this.map { it.toEntity() }

