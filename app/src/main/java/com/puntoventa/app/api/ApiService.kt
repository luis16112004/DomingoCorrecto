package com.puntoventa.app.api

import com.puntoventa.app.api.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    // Autenticación
    @POST("api/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @POST("api/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<Map<String, String>>
    
    // Proveedores
    @GET("api/providers")
    suspend fun getProviders(@Header("Authorization") token: String): Response<ProvidersResponse>
    
    @GET("api/providers/{id}")
    suspend fun getProvider(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<ApiResponse<Provider>>
    
    @POST("api/providers")
    suspend fun createProvider(
        @Header("Authorization") token: String,
        @Body provider: Provider
    ): Response<ApiResponse<Provider>>
    
    @PUT("api/providers/{id}")
    suspend fun updateProvider(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body provider: Provider
    ): Response<ApiResponse<Provider>>
    
    @DELETE("api/providers/{id}")
    suspend fun deleteProvider(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Map<String, String>>
    
    // Productos
    @GET("api/products")
    suspend fun getProducts(@Header("Authorization") token: String): Response<ProductsResponse>
    
    @GET("api/products/{id}")
    suspend fun getProduct(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<ApiResponse<Product>>
    
    @POST("api/products")
    suspend fun createProduct(
        @Header("Authorization") token: String,
        @Body product: Product
    ): Response<ApiResponse<Product>>
    
    @PUT("api/products/{id}")
    suspend fun updateProduct(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body product: Product
    ): Response<ApiResponse<Product>>
    
    @DELETE("api/products/{id}")
    suspend fun deleteProduct(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Map<String, String>>
    
    // Clientes
    @GET("api/customers")
    suspend fun getCustomers(@Header("Authorization") token: String): Response<CustomersResponse>
    
    @GET("api/customers/{id}")
    suspend fun getCustomer(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<ApiResponse<Customer>>
    
    @POST("api/customers")
    suspend fun createCustomer(
        @Header("Authorization") token: String,
        @Body customer: Customer
    ): Response<ApiResponse<Customer>>
    
    @PUT("api/customers/{id}")
    suspend fun updateCustomer(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body customer: Customer
    ): Response<ApiResponse<Customer>>
    
    @DELETE("api/customers/{id}")
    suspend fun deleteCustomer(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Map<String, String>>
    
    // Ventas
    @GET("api/sales")
    suspend fun getSales(@Header("Authorization") token: String): Response<SalesResponse>
    
    @GET("api/sales/{id}")
    suspend fun getSale(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<ApiResponse<Sale>>
    
    @POST("api/sales")
    suspend fun createSale(
        @Header("Authorization") token: String,
        @Body sale: CreateSaleRequest
    ): Response<ApiResponse<Sale>>
    
    @PUT("api/sales/{id}")
    suspend fun updateSale(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body sale: Sale
    ): Response<ApiResponse<Sale>>
    
    @DELETE("api/sales/{id}")
    suspend fun deleteSale(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Map<String, String>>
}

