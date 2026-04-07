package com.example.uppmanageapp1.shop

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites WHERE productId = :productId LIMIT 1")
    fun getFavoriteFlow(productId: String): Flow<FavoriteEntity?>

    @Query("SELECT * FROM favorites WHERE productId = :productId LIMIT 1")
    suspend fun getFavoriteOnce(productId: String): FavoriteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE productId = :productId")
    suspend fun deleteFavorite(productId: String)
}

