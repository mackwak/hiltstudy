package com.example.uppmanageapp1.shop

import kotlinx.coroutines.delay

class FakeShopRepository : ShopRepository {
    override suspend fun getProducts(): List<Product> {
        delay(300) // 모의 네트워크 지연
        return listOf(
            Product(id = "1", title = "T-Shirt", price = "$19.99", imageUrl = null),
            Product(id = "2", title = "Sneakers", price = "$79.99", imageUrl = null),
            Product(id = "3", title = "Mug", price = "$9.99", imageUrl = null)
        )
    }
}

