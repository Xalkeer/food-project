package com.example.food_project.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "meals",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["category"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
])
data class Meal(
    @PrimaryKey() val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "category") val category: Int,
    @ColumnInfo(name = "area") val area: String,
    @ColumnInfo(name = "instructions") val instructions: String,
    @ColumnInfo(name = "media") val media: String,
    @ColumnInfo(name = "ingredients") val ingredients : String,
    @ColumnInfo(name = "measures") val measures : String
)

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey() val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "media") val media: String,
)