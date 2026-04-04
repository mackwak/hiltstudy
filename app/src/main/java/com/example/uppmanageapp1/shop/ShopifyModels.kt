package com.example.uppmanageapp1.shop

// 간단한 제품 모델
data class Product(
    val id: String,
    val title: String,
    val price: String,
    val imageUrl: String? = null
)

