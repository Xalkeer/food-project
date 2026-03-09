package com.example.food_project.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.food_project.data.api.entity.CategoryEntity

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getAll() : List<CategoryEntity>

    @Insert
    fun insertCategory(category: CategoryEntity)

    @Delete
    fun delete(category: CategoryEntity)
}