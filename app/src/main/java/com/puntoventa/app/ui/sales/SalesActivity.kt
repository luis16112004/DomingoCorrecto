package com.puntoventa.app.ui.sales

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.puntoventa.app.R
import com.puntoventa.app.api.RetrofitClient
import com.puntoventa.app.databinding.ActivitySalesBinding
import com.puntoventa.app.MainActivity
import com.puntoventa.app.utils.TokenManager
import kotlinx.coroutines.launch

class SalesActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySalesBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var adapter: SalesAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalesBinding.inflate(layoutInflater)
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
        loadSales()
    }
    
    private fun setupRecyclerView() {
        adapter = SalesAdapter(emptyList())
        binding.rvSales.layoutManager = LinearLayoutManager(this)
        binding.rvSales.adapter = adapter
    }
    
    private fun setupListeners() {
        binding.fabAddSale.setOnClickListener {
            startActivity(Intent(this, CreateSaleActivity::class.java))
        }
        
        binding.swipeRefresh.setOnRefreshListener {
            loadSales()
        }
    }
    
    private fun loadSales() {
        val token = tokenManager.getToken() ?: return
        
        binding.swipeRefresh.isRefreshing = true
        binding.progressBar.visibility = android.view.View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getSales("Bearer $token")
                
                if (response.isSuccessful && response.body() != null) {
                    val sales = response.body()!!.data
                    adapter.updateSales(sales)
                    
                    if (sales.isEmpty()) {
                        binding.tvEmpty.visibility = android.view.View.VISIBLE
                    } else {
                        binding.tvEmpty.visibility = android.view.View.GONE
                    }
                } else {
                    Toast.makeText(this@SalesActivity, "Error al cargar ventas", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SalesActivity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                binding.swipeRefresh.isRefreshing = false
                binding.progressBar.visibility = android.view.View.GONE
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        loadSales()
    }
}

