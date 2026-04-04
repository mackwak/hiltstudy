// Creating DefaultShopRepository implementation for ShopRepository
package com.example.uppmanageapp1.shop

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultShopRepository @Inject constructor(
    private val service: ShopifyService
) : ShopRepository {
    override suspend fun getProducts(): List<Product> {
        return try {
            val response = service.getProducts()
            if (response.isSuccessful) {
                response.body()?.products?.map { it.toDomain() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("aa", "Exception while fetching products: ${e.message}\n${e.stackTraceToString()}")
            emptyList()
        }
    }
}
