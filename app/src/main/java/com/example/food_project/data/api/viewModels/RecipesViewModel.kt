package com.example.food_project.data.api.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_project.data.api.entity.RecipeEntity
import com.example.food_project.data.api.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.logging.Logger

class RecipesViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val logger = Logger.getLogger("RecipesViewModel")

    // État UI : observe directement le Flow Room (source de vérité)
    private val _uiState = MutableStateFlow<List<RecipeEntity>>(emptyList())
    val uiState: StateFlow<List<RecipeEntity>> = _uiState.asStateFlow()

    // État de chargement
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // État d'erreur
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        // Observer le Flow Room : l'UI se met à jour automatiquement
        viewModelScope.launch {
            repository.recipes
                .catch { e ->
                    println("🔴 [ViewModel] Erreur Flow Room: ${e.message}")
                    _errorMessage.value = "Erreur BDD: ${e.message}"
                }
                .collect { recipes ->
                    println("🟢 [ViewModel] Room a notifié ${recipes.size} recettes")
                    _uiState.value = recipes
                }
        }
    }

    /**
     * Rafraîchit les recettes depuis l'API et met à jour la BDD Room.
     * L'UI se met à jour automatiquement via le Flow observé dans init.
     */
    fun searchRecipes(query: String) {
        viewModelScope.launch {
            try {
                println("🔵 [ViewModel] searchRecipes START avec query='$query'")
                _isLoading.value = true
                _errorMessage.value = null

                println("🔵 [ViewModel] Appel repository.refreshRecipes('$query')")
                // Rafraîchit depuis l'API → insère dans Room → Flow notifie automatiquement
                repository.refreshRecipes(query)
                println("🟢 [ViewModel] repository.refreshRecipes terminé (Room mis à jour)")

                _isLoading.value = false
                println("🟢 [ViewModel] searchRecipes TERMINÉ avec succès")

            } catch (e: Exception) {
                println("🔴 [ViewModel] ERREUR: ${e.message}")
                logger.severe("Erreur lors de la recherche de recettes: ${e.message}")
                e.printStackTrace()
                _errorMessage.value = "Erreur: ${e.message}"
                _isLoading.value = false
                println("🔴 [ViewModel] searchRecipes TERMINÉ avec ERREUR")
            }
        }
    }
}