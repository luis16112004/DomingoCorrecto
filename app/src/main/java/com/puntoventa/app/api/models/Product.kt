package com.puntoventa.app.api.models

data class Product(
    val id: String? = null,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val category: String,
    val sku: String? = null,
    val providerId: String? = null,
    val userId: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class ProductsResponse(
    val message: String,
    val data: List<Product>
)

