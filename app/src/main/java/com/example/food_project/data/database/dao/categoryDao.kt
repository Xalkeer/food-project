package com.example.food_project.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.food_project.data.api.entity.CategoryEntity
import com.example.food_project.data.api.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY strCategory")
    fun getAll() : Flow<List<CategoryEntity>>

    @Insert
    fun insertCategory(category: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategories(category: List<CategoryEntity>)

    @Delete
    fun delete(category: CategoryEntity)
}