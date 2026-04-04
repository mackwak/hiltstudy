package com.example.uppmanageapp1.shop

interface ShopRepository {
    suspend fun getProducts(): List<Product>
}

