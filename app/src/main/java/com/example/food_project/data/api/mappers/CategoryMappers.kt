package com.example.food_project.data.api.mappers

import com.example.food_project.data.api.dto.CategoryDTO
import com.example.food_project.data.api.entity.CategoryEntity

// Mapper extensions for Category-related models

fun CategoryDTO.toEntity(): CategoryEntity = CategoryEntity(
    id = this.idCategory,
    strCategory = this.strCategory,
    strCategoryThumb = this.strCategoryThumb
)
fun CategoryEntity.toDTO(): CategoryDTO = CategoryDTO(
    idCategory = this.id,
    strCategory = this.strCategory,
    strCategoryThumb = this.strCategoryThumb
)

fun List<CategoryEntity>.toDTOs(): List<CategoryDTO> = this.map { it.toDTO() }

fun List<CategoryDTO>.toEntities(): List<CategoryEntity> = this.map { it.toEntity() }

