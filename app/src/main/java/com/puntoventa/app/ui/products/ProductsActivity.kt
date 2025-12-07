package com.puntoventa.app.ui.products

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.puntoventa.app.R
import com.puntoventa.app.api.RetrofitClient
import com.puntoventa.app.databinding.ActivityProductsBinding
import com.puntoventa.app.MainActivity
import com.puntoventa.app.utils.TokenManager
import kotlinx.coroutines.launch

class ProductsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductsBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var adapter: ProductsAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        tokenManager = TokenManager(this)
        
        // Si no está logueado, ir a login
        if (!tokenManager.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }
        
        setupRecyclerView()
        setupListeners()
        loadProducts()
    }
    
    private fun setupRecyclerView() {
        adapter = ProductsAdapter(emptyList())
        binding.rvProducts.layoutManager = LinearLayoutManager(this)
        binding.rvProducts.adapter = adapter
    }
    
    private fun setupListeners() {
        binding.fabAddProduct.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }
        
        binding.swipeRefresh.setOnRefreshListener {
            loadProducts()
        }
    }
    
    private fun loadProducts() {
        val token = tokenManager.getToken() ?: return
        
        binding.swipeRefresh.isRefreshing = true
        binding.progressBar.visibility = android.view.View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getProducts("Bearer $token")
                
                if (response.isSuccessful && response.body() != null) {
                    val products = response.body()!!.data
                    adapter.updateProducts(products)
                    
                    if (products.isEmpty()) {
                        binding.tvEmpty.visibility = android.view.View.VISIBLE
                    } else {
                        binding.tvEmpty.visibility = android.view.View.GONE
                    }
                } else {
                    Toast.makeText(this@ProductsActivity, "Error al cargar productos", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProductsActivity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                binding.swipeRefresh.isRefreshing = false
                binding.progressBar.visibility = android.view.View.GONE
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        loadProducts()
    }
}

