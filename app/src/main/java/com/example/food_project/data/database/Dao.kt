package com.example.food_project.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MealDao {
    @Query("SELECT * FROM meals")
    fun getAll() : List<Meal>

    @Query(
        "SELECT * FROM meals m " +
                "LEFT JOIN categories c ON m.category = c.id " +
                "WHERE c.title = :categoryTitle")
    fun getMealByCategory(categoryTitle: String) : List<Meal>

    @Insert
    fun insertMeal(meal: Meal)

    @Delete
    fun delete(meals: Meal)
}

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getAll() : List<Category>

    @Insert
    fun insertCategory(category: Category)

    @Delete
    fun delete(category: Category)
}