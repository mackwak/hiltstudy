package com.example.uppmanageapp1.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ShopUiState {
    object Loading : ShopUiState
    data class Success(val products: List<Product>) : ShopUiState
    data class Error(val message: String) : ShopUiState
}

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val repository: ShopRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ShopUiState>(ShopUiState.Loading)
    val uiState: StateFlow<ShopUiState> = _uiState

    init {
        loadProducts()
    }

    fun loadProducts() {
        _uiState.value = ShopUiState.Loading
        viewModelScope.launch {
            try {
                val products = repository.getProducts()
                _uiState.value = ShopUiState.Success(products)
            } catch (e: Exception) {
                _uiState.value = ShopUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

