package com.example.uppmanageapp1.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteDao: FavoriteDao
) : ViewModel() {

    fun isFavoriteFlow(productId: String): Flow<Boolean> {
        return favoriteDao.getFavoriteFlow(productId)
            .map { it != null }
            .distinctUntilChanged()
    }

    fun toggleFavorite(productId: String) {
        viewModelScope.launch {
            val existing = favoriteDao.getFavoriteOnce(productId)
            if (existing == null) {
                favoriteDao.insertFavorite(FavoriteEntity(productId))
            } else {
                favoriteDao.deleteFavorite(productId)
            }
        }
    }

    fun addFavorite(productId: String) {
        viewModelScope.launch {
            favoriteDao.insertFavorite(FavoriteEntity(productId))
        }
    }

    fun removeFavorite(productId: String) {
        viewModelScope.launch {
            favoriteDao.deleteFavorite(productId)
        }
    }
}

