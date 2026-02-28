package com.example.food_project

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.food_project.data.api.CategoryViewModel
import com.example.food_project.data.api.repository.CategoriesRepository
import com.example.food_project.data.api.services.CategoriesService
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Factory très simple pour fournir un CategoryViewModel à MainActivity
 * en initialisant manuellement le service distant et le repository.
 *
 * Version sans base de données : le repository ne stocke plus en local,
 * il sert uniquement à appeler l'API distante.
 */
class CategoryViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            println("[CategoryViewModelFactory] Création du HttpClient et du CategoriesService")

            val client = HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    })
                }
            }

            val apiService = CategoriesService(client)
            val repository = CategoriesRepository(apiService, dao = null)

            println("[CategoryViewModelFactory] CategoryViewModel créé")
            return CategoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${'$'}modelClass")
    }
}
