package com.example.food_project.data.api.local

import androidx.room.*
import com.example.food_project.data.api.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)

    @Query("DELETE FROM recipes")
    suspend fun clearAll()
}