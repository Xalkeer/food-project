package com.example.food_project.data.api.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_project.data.api.entity.CategoryEntity
import com.example.food_project.data.api.entity.RecipeEntity
import com.example.food_project.data.api.repository.RecipeRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.logging.Logger

@OptIn(FlowPreview::class)
class RecipesViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val logger = Logger.getLogger("RecipesViewModel")
    private val _uiState = MutableStateFlow<List<RecipeEntity>>(emptyList())
    val uiState: StateFlow<List<RecipeEntity>> = _uiState.asStateFlow()
    private var _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _isInitialLoading = MutableStateFlow(true)
    val isInitialLoading = _isInitialLoading.asStateFlow()
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    private val _selectedRecipe = MutableStateFlow<RecipeEntity?>(null)
    val selectedRecipe: StateFlow<RecipeEntity?> = _selectedRecipe.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")

    val selectedCategory = _selectedCategory.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredRecipes: StateFlow<List<RecipeEntity>> = combine(
        _uiState,
        _searchQuery,
        _selectedCategory
    ) { recipes, query, selectedCat ->
        recipes.filter { recipe ->
            val matchesQuery = query.isBlank() || recipe.title.contains(query, ignoreCase = true)

            val matchesCategory = if (selectedCat == "All" || selectedCat.isBlank()) {
                true
            } else {
                recipe.category?.equals(selectedCat, ignoreCase = true) == true
            }

            matchesQuery && matchesCategory
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun selectRecipe(recipe: RecipeEntity) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val updatedRecipe = repository.refreshRecipeById(recipe.id)
                _selectedRecipe.value = updatedRecipe
                } catch (e: Exception) {
                _errorMessage.value = "Impossible de charger les détails : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }

    }

    fun clearSelectedRecipe() {
        _selectedRecipe.value = null
    }

    init {
        viewModelScope.launch {
            repository.recipes
                .catch { e ->
                    println("🔴 [ViewModel] Erreur Flow Room: ${e.message}")
                    _errorMessage.value = "Erreur BDD: ${e.message}"
                }
                .collect { recipes ->
                    _uiState.value = recipes
                    _isInitialLoading.value = false
                }
        }

        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .filter { it.isNotBlank() }
                .collect { query ->
                    searchRecipesByName(query)
                    _isLoading.value = false
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
                _selectedCategory.value = ""

                repository.refreshRecipes(query)
                println("🟢 [ViewModel] repository.refreshRecipes terminé (Room mis à jour)")

                _isLoading.value = false
            } catch (e: Exception) {
                println("🔴 [ViewModel] ERREUR: ${e.message}")
                logger.severe("Erreur lors de la recherche de recettes: ${e.message}")
                e.printStackTrace()
                _errorMessage.value = "Erreur: ${e.message}"
                _isLoading.value = false
            }
        }
    }
    fun searchRecipesById(id: String) {
        viewModelScope.launch {
            try {
                println("🔵 [ViewModel] searchRecipesById START avec id='$id'")
                _isLoading.value = true
                _errorMessage.value = null

                println("🔵 [ViewModel] Appel repository.refreshRecipeById('$id')")
                repository.refreshRecipeById(id)
                println("🟢 [ViewModel] repository.refreshRecipeById terminé (Room mis à jour)")
                _isLoading.value = false
                println("🟢 [ViewModel] searchRecipesById TERMINÉ avec succès")
            } catch (e: Exception) {
                println("🔴 [ViewModel] ERREUR: ${e.message}")
                logger.severe("Erreur lors de la recherche de recette par ID: ${e.message}")
                e.printStackTrace()
                _errorMessage.value = "Erreur: ${e.message}"
                _isLoading.value = false
                println("🔴 [ViewModel] searchRecipesById TERMINÉ avec ERREUR")
            }
        }
    }

    fun searchRecipesByCategory(category: CategoryEntity) {
        viewModelScope.launch {
            try {
                println("🔵 [ViewModel] searchRecipesByCategory START avec category='$category'")
                _isLoading.value = true
                _errorMessage.value = null
                _selectedCategory.value = category.strCategory

                repository.refreshRecipesByCategory(category.strCategory)
                println("🟢 [ViewModel] repository.refreshRecipesByCategory terminé (Room mis à jour)")
                _isLoading.value = false
            } catch (e: Exception) {
                println("🔴 [ViewModel] ERREUR: ${e.message}")
                logger.severe("Erreur lors de la recherche de recettes par catégorie: ${e.message}")
                e.printStackTrace()
                _errorMessage.value = "Erreur: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun searchRecipesByName(name: String) {
        viewModelScope.launch {
            try {
                println("🔵 [ViewModel] searchRecipesByName START avec name='$name'")
                _isLoading.value = true
                _errorMessage.value = null
                _selectedCategory.value = ""

                repository.refreshRecipes(name)
                println("🟢 [ViewModel] repository.refreshRecipes (by name) terminé (Room mis à jour)")
                _isLoading.value = false
            } catch (e: Exception) {
                println("🔴 [ViewModel] ERREUR: ${e.message}")
                logger.severe("Erreur lors de la recherche de recettes par nom: ${e.message}")
                e.printStackTrace()
                _errorMessage.value = "Erreur: ${e.message}"
                _isLoading.value = false
            }
        }
    }
}