package com.example.food_project.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import com.example.food_project.data.api.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<RecipeEntity>>

    @RewriteQueriesToDropUnusedColumns
    @Query(
        "SELECT m.* FROM recipes m " +
                "LEFT JOIN categories c ON m.category = c.id " +
                "WHERE c.strCategory = :strCategory"
    )
    fun getMealByCategory(strCategory: String): Flow<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: RecipeEntity)

    @Delete
    suspend fun delete(meal: RecipeEntity)

    @Query("DELETE FROM recipes")
    suspend fun clearAll()
}
