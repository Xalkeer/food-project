package com.example.food_project.data.api.repository

import com.example.food_project.data.api.entity.RecipeEntity
import com.example.food_project.data.database.RecipeDao
import com.example.food_project.data.api.mappers.toEntities
import com.example.food_project.data.api.services.RecipesService
import kotlinx.coroutines.flow.Flow

class RecipeRepository(
    val api: RecipesService,
    private val dao: RecipeDao
) {
    val recipes: Flow<List<RecipeEntity>> = dao.getAllRecipes()

    suspend fun refreshRecipes(query: String) {
        try {
            val remoteData = api.searchRecipes(query)
            remoteData.forEach { dto ->
                println("  - id=${dto.idMeal}, title=${dto.strMeal}, thumb=${dto.strMealThumb}, category=${dto.strCategory}")
            }
            val entities = remoteData.toEntities()
            println("🔵 [Repository] Insertion de ${entities.size} recettes dans Room...")
            dao.clearAll()
            dao.insertRecipes(entities)
            println("🟢 [Repository] ${entities.size} recettes insérées dans la BDD interne")

        } catch (e: Exception) {
            println("🔴 [Repository] ERREUR lors du refresh: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }


    suspend fun refreshRecipesByCategory(category: String) {
        try {
            println("🔵 [Repository] refreshRecipesByCategory START pour category='$category'")
            val remoteData = api.searchRecipesByCategory(category)
            val entities = remoteData.toEntities()
            println("🔵 [Repository] Insertion de ${entities.size} recettes dans Room...")
            dao.clearAll()
            dao.insertRecipes(entities)
            println("🟢 [Repository] ${entities.size} recettes insérées dans la BDD interne")
        } catch (e: Exception) {
            println("🔴 [Repository] ERREUR dans refreshRecipesByCategory: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    suspend fun refreshRecipeById(id: String) {
            try {
                println("🔵 [Repository] refreshRecipeById START pour id='$id'")
                val dto = api.getRecipeById(id)
                if (dto != null) {
                    val entity = dto.toEntities().firstOrNull()
                    if (entity != null) {
                        println("🔵 [Repository] Mise à jour de la recette id=${entity.id} dans Room...")
                        dao.insertRecipes(listOf(entity))
                        println("🟢 [Repository] Recette id=${entity.id} mise à jour dans la BDD interne")
                    } else {
                        println("🔴 [Repository] Aucune entité créée à partir du DTO pour id='$id'")
                    }
                } else {
                    println("🔴 [Repository] Aucune recette trouvée pour id='$id' via l'API")
                }
            } catch (e: Exception) {
                println("🔴 [Repository] ERREUR dans refreshRecipeById: ${e.message}")
                e.printStackTrace()
            }
    }

    suspend fun refreshRecipeByName(name: String) {
        try {
            println("🔵 [Repository] refreshRecipeByName START pour name='$name'")
            val remoteData = api.searchRecipesByName(name)
            val entities = remoteData.toEntities()
            println("🔵 [Repository] Insertion de ${entities.size} recettes dans Room...")
            dao.clearAll()
            dao.insertRecipes(entities)
            println("🟢 [Repository] ${entities.size} recettes insérées dans la BDD interne")
        } catch (e: Exception) {
            println("🔴 [Repository] ERREUR dans refreshRecipeByName: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

}