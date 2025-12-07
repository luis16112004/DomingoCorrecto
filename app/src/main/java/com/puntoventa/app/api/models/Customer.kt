package com.puntoventa.app.api.models

data class Customer(
    val id: String? = null,
    val name: String,
    val email: String? = null,
    val phoneNumber: String,
    val address: String? = null,
    val city: String? = null,
    val state: String? = null,
    val postalCode: String? = null,
    val userId: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class CustomersResponse(
    val message: String,
    val data: List<Customer>
)

