package com.puntoventa.app.api.models

data class SaleItem(
    val productId: String,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double
)

data class Sale(
    val id: String? = null,
    val customerId: String? = null,
    val customerName: String? = null,
    val items: List<SaleItem>,
    val subtotal: Double,
    val tax: Double = 0.0,
    val total: Double,
    val paymentMethod: String? = "efectivo", // efectivo, tarjeta, transferencia
    val status: String? = "completada", // completada, cancelada, pendiente
    val userId: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class SalesResponse(
    val message: String,
    val data: List<Sale>
)

data class CreateSaleRequest(
    val customerId: String? = null,
    val customerName: String? = null,
    val items: List<SaleItem>,
    val paymentMethod: String? = "efectivo"
)

