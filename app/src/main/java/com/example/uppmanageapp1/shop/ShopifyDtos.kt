package com.example.uppmanageapp1.shop

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductsResponse(
    @Json(name = "products") val products: List<ProductDto> = emptyList()
)

@JsonClass(generateAdapter = true)
data class ProductDto(
    val id: Long,
    val title: String,
    @Json(name = "body_html") val bodyHtml: String? = null,
    val variants: List<VariantDto> = emptyList(),
    val images: List<ImageDto> = emptyList()
)

@JsonClass(generateAdapter = true)
data class VariantDto(
    val id: Long,
    val price: String? = null
)

@JsonClass(generateAdapter = true)
data class ImageDto(
    val id: Long,
    val src: String? = null
)

// Mapping to domain model
fun ProductDto.toDomain(): Product = Product(
    id = id.toString(),
    title = title,
    price = variants.firstOrNull()?.price ?: "0",
    imageUrl = images.firstOrNull()?.src
)

