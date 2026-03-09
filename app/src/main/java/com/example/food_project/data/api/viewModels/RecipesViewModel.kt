package com.example.food_project.data.api.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_project.data.api.dto.RecipesDTO
import com.example.food_project.data.api.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.logging.Logger

class RecipesViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val logger = Logger.getLogger("RecipesViewModel")

    // État UI interne : recettes à afficher
    private val _uiState = MutableStateFlow<List<RecipesDTO>>(emptyList())
    val uiState: StateFlow<List<RecipesDTO>> = _uiState.asStateFlow()

    // État de chargement
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // État d'erreur
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun searchRecipes(query: String) {
        viewModelScope.launch {
            try {
                println("🔵 [ViewModel] searchRecipes START avec query='$query'")
                _isLoading.value = true
                _errorMessage.value = null

                println("🔵 [ViewModel] Appel repository.refreshRecipes('$query')")
                repository.refreshRecipes(query)
                println("🟢 [ViewModel] repository.refreshRecipes terminé")

                println("🔵 [ViewModel] Appel repository.api.searchRecipes('$query')")
                // Pour l'instant, affiche directement les DTOs
                // Dans le futur, on pourra récupérer depuis Room (dao.getAllRecipes())
                val recipes = repository.api.searchRecipes(query)

                println("🟢 [ViewModel] Reçu ${recipes.size} recettes de l'API")

                // Log console pour vérifier le contenu de l'API
                println("🍔 Recettes reçues (${recipes.size}):")
                recipes.forEachIndexed { index, dto ->
                    println(
                        """
                        [$index] ------
                        ID: ${dto.idMeal}
                        Titre: ${dto.strMeal}
                        Catégorie: ${dto.strCategory}
                        Zone: ${dto.strArea}
                        Image: ${dto.strMealThumb}
                        Instructions: ${dto.strInstructions?.take(50)}...
                        """.trimIndent()
                    )
                }

                println("🔵 [ViewModel] Mise à jour du _uiState avec ${recipes.size} recettes")
                _uiState.value = recipes
                println("🟢 [ViewModel] _uiState mis à jour")

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