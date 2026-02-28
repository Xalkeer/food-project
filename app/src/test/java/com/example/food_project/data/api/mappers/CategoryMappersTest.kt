package com.example.food_project.data.api.mappers

import com.example.food_project.data.api.dto.CategoryDTO
import com.example.food_project.data.api.entity.CategoryEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class CategoryMappersTest {

    @Test
    fun `toEntity should map fields correctly`() {
        val dto = CategoryDTO(
            id = "1",
            name = "Beef",
            imageUrl = "https://example.com/beef.png"
        )

        val entity: CategoryEntity = dto.toEntity()

        assertEquals(dto.id, entity.id)
        assertEquals(dto.name, entity.strCategory)
        assertEquals(dto.imageUrl, entity.strCategoryThumb)
    }

    @Test
    fun `toEntities should map list correctly`() {
        val dtos = listOf(
            CategoryDTO("1", "Beef", "url1"),
            CategoryDTO("2", "Chicken", "url2")
        )

        val entities = dtos.toEntities()

        assertEquals(dtos.size, entities.size)
        assertEquals(dtos[0].name, entities[0].strCategory)
        assertEquals(dtos[1].name, entities[1].strCategory)
    }
}

