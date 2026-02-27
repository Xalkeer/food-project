package com.example.food_project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_project.data.api.services.CategoriesService
import com.example.food_project.data.api.entity.CategoryEntity
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*

class MainViewModel : ViewModel() {

    private val _categories = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val categories: StateFlow<List<CategoryEntity>> = _categories

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            val client = HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    })
                }
            }

            try {
                val service = CategoriesService(client)
                val dtos = service.getCategories()
                val entities = dtos.map { dto ->
                    CategoryEntity(
                        id = dto.id,
                        strCategory = dto.name,
                        strCategoryThumb = dto.imageUrl
                    )
                }
                _categories.value = entities
            } catch (e: Exception) {
                // laisser la liste vide en cas d'erreur
            } finally {
                client.close()
            }
        }
    }
}