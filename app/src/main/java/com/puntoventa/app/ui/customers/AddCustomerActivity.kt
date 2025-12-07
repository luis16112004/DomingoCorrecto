package com.puntoventa.app.ui.customers

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.puntoventa.app.R
import com.puntoventa.app.api.RetrofitClient
import com.puntoventa.app.api.models.Customer
import com.puntoventa.app.databinding.ActivityAddCustomerBinding
import com.puntoventa.app.utils.TokenManager
import kotlinx.coroutines.launch

class AddCustomerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddCustomerBinding
    private lateinit var tokenManager: TokenManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        tokenManager = TokenManager(this)
        
        binding.btnSave.setOnClickListener {
            saveCustomer()
        }
    }
    
    private fun saveCustomer() {
        val name = binding.etName.text.toString().trim()
        val phoneNumber = binding.etPhoneNumber.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val city = binding.etCity.text.toString().trim()
        val state = binding.etState.text.toString().trim()
        val postalCode = binding.etPostalCode.text.toString().trim()
        
        // Validaciones básicas
        if (name.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Por favor completa los campos obligatorios (Nombre y Teléfono)", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (phoneNumber.length != 10) {
            Toast.makeText(this, "El teléfono debe tener 10 dígitos", Toast.LENGTH_SHORT).show()
            return
        }
        
        val token = tokenManager.getToken() ?: return
        
        binding.btnSave.isEnabled = false
        binding.progressBar.visibility = android.view.View.VISIBLE
        
        val customer = Customer(
            name = name,
            phoneNumber = phoneNumber,
            email = if (email.isNotEmpty()) email else null,
            address = if (address.isNotEmpty()) address else null,
            city = if (city.isNotEmpty()) city else null,
            state = if (state.isNotEmpty()) state else null,
            postalCode = if (postalCode.isNotEmpty()) postalCode else null,
            userId = tokenManager.getUserId()
        )
        
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.createCustomer("Bearer $token", customer)
                
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(this@AddCustomerActivity, "Cliente creado exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    Toast.makeText(this@AddCustomerActivity, "Error: $errorBody", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AddCustomerActivity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                binding.btnSave.isEnabled = true
                binding.progressBar.visibility = android.view.View.GONE
            }
        }
    }
}

