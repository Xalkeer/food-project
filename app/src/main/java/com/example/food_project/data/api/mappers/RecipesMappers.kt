package com.example.food_project.data.api.mappers

import com.example.food_project.data.api.dto.RecipesDTO
import com.example.food_project.data.api.entity.RecipeEntity

fun RecipesDTO.toEntity(forcedCategory: String? = null): RecipeEntity {
    val ingredients = listOfNotNull(
        strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
        strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
        strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15,
        strIngredient16, strIngredient17, strIngredient18, strIngredient19, strIngredient20
    ).filter { it.isNotEmpty() }

    val measures = listOfNotNull(
        strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
        strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
        strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15,
        strMeasure16, strMeasure17, strMeasure18, strMeasure19, strMeasure20
    ).filter { it.isNotEmpty() }

    return RecipeEntity(
        id = idMeal,
        title = strMeal,
        imageUrl = strMealThumb,
        description = strInstructions.orEmpty(),
        category = strCategory ?: forcedCategory,
        area = strArea,
        tags = strTags,
        youtube = strYoutube,
        source = strSource,
        dateModified = dateModified,
        ingredients = ingredients.ifEmpty { null },
        measures = measures.ifEmpty { null }
    )
}

fun List<RecipesDTO>.toEntities(forcedCategory: String? = null): List<RecipeEntity> = map { it.toEntity(forcedCategory) }

fun RecipesDTO.toEntities(): List<RecipeEntity> = listOf(this.toEntity())
