package com.puntoventa.app.ui.customers

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.puntoventa.app.R
import com.puntoventa.app.api.RetrofitClient
import com.puntoventa.app.databinding.ActivityCustomersBinding
import com.puntoventa.app.MainActivity
import com.puntoventa.app.utils.TokenManager
import kotlinx.coroutines.launch

class CustomersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomersBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var adapter: CustomersAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomersBinding.inflate(layoutInflater)
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
        loadCustomers()
    }
    
    private fun setupRecyclerView() {
        adapter = CustomersAdapter(emptyList())
        binding.rvCustomers.layoutManager = LinearLayoutManager(this)
        binding.rvCustomers.adapter = adapter
    }
    
    private fun setupListeners() {
        binding.fabAddCustomer.setOnClickListener {
            startActivity(Intent(this, AddCustomerActivity::class.java))
        }
        
        binding.swipeRefresh.setOnRefreshListener {
            loadCustomers()
        }
    }
    
    private fun loadCustomers() {
        val token = tokenManager.getToken() ?: return
        
        binding.swipeRefresh.isRefreshing = true
        binding.progressBar.visibility = android.view.View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getCustomers("Bearer $token")
                
                if (response.isSuccessful && response.body() != null) {
                    val customers = response.body()!!.data
                    adapter.updateCustomers(customers)
                    
                    if (customers.isEmpty()) {
                        binding.tvEmpty.visibility = android.view.View.VISIBLE
                    } else {
                        binding.tvEmpty.visibility = android.view.View.GONE
                    }
                } else {
                    Toast.makeText(this@CustomersActivity, "Error al cargar clientes", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CustomersActivity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                binding.swipeRefresh.isRefreshing = false
                binding.progressBar.visibility = android.view.View.GONE
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        loadCustomers()
    }
}

