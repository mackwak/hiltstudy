// ...new file...
package com.example.uppmanageapp1.shop

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

// Local test Product type used only in tests
data class Product(val id: String, val title: String)

class ShopRepositoryTest {

    @Test
    fun returnsProductsWhenRepositoryProvidesNonEmptyList() = runBlocking {
        val repo = object : ShopRepository {
            override suspend fun getProducts(): List<Product> = listOf(Product("p1", "Product 1"), Product("p2", "Product 2"))
        }

        val products = repo.getProducts()
        assertEquals(2, products.size)
        assertEquals("p1", products[0].id)
    }

    @Test
    fun returnsEmptyListWhenRepositoryProvidesEmptyList() = runBlocking {
        val repo = object : ShopRepository {
            override suspend fun getProducts(): List<Product> = emptyList()
        }

        val products = repo.getProducts()
        assertTrue(products.isEmpty())
    }

    @Test
    fun propagatesExceptionWhenRepositoryThrows() = runBlocking {
        val expected = RuntimeException("network error")
        val repo = object : ShopRepository {
            override suspend fun getProducts(): List<Product> {
                throw expected
            }
        }

        try {
            repo.getProducts()
            fail("Expected exception was not thrown")
        } catch (e: Exception) {
            assertSame(expected, e)
        }
    }
}

