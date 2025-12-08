package com.puntoventa.app.ui.products

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.puntoventa.app.R
import com.puntoventa.app.api.RetrofitClient
import com.puntoventa.app.api.models.Product
import com.puntoventa.app.databinding.ActivityAddProductBinding
import com.puntoventa.app.utils.TokenManager
import kotlinx.coroutines.launch

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var tokenManager: TokenManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        tokenManager = TokenManager(this)
        
        binding.btnSave.setOnClickListener {
            saveProduct()
        }
    }
    
    private fun saveProduct() {
        val name = binding.etName.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val priceText = binding.etPrice.text.toString().trim()
        val stockText = binding.etStock.text.toString().trim()
        val category = binding.etCategory.text.toString().trim()
        val sku = binding.etSku.text.toString().trim()
        
        // Validaciones básicas
        if (name.isEmpty() || description.isEmpty() || priceText.isEmpty() || 
            stockText.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }
        
        val price = try {
            priceText.toDouble()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "El precio debe ser un número válido", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (price <= 0) {
            Toast.makeText(this, "El precio debe ser mayor a 0", Toast.LENGTH_SHORT).show()
            return
        }
        
        val stock = try {
            stockText.toInt()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "El stock debe ser un número válido", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (stock < 0) {
            Toast.makeText(this, "El stock no puede ser negativo", Toast.LENGTH_SHORT).show()
            return
        }
        
        val token = tokenManager.getToken() ?: return
        
        binding.btnSave.isEnabled = false
        binding.progressBar.visibility = android.view.View.VISIBLE
        
        val product = Product(
            name = name,
            description = description,
            price = price,
            stock = stock,
            category = category,
            sku = if (sku.isNotEmpty()) sku else null,
            userId = tokenManager.getUserId()
        )
        
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.createProduct("Bearer $token", product)
                
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(this@AddProductActivity, "Producto creado exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val errorMessage = if (response.code() == 404) {
                        "El endpoint de productos no está disponible en el servidor. Contacta al administrador para habilitarlo."
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "Error desconocido (Código: ${response.code()})"
                        "Error: $errorBody"
                    }
                    Toast.makeText(this@AddProductActivity, errorMessage, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AddProductActivity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                binding.btnSave.isEnabled = true
                binding.progressBar.visibility = android.view.View.GONE
            }
        }
    }
}


