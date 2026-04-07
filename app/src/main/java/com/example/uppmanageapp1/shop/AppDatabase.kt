package com.example.uppmanageapp1.shop

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.uppmanageapp1.shop.FavoriteDao
import com.example.uppmanageapp1.shop.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}