package com.puntoventa.app.ui.sales

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.puntoventa.app.R
import com.puntoventa.app.api.RetrofitClient
import com.puntoventa.app.api.models.*
import com.puntoventa.app.databinding.ActivityCreateSaleBinding
import com.puntoventa.app.databinding.DialogAddProductBinding
import com.puntoventa.app.utils.TokenManager
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

class CreateSaleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateSaleBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var adapter: SaleItemsAdapter
    private val saleItems = mutableListOf<SaleItem>()
    private var products = listOf<Product>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateSaleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        tokenManager = TokenManager(this)
        
        setupRecyclerView()
        setupPaymentMethodSpinner()
        setupListeners()
        loadProducts()
        updateTotals()
    }
    
    private fun setupRecyclerView() {
        adapter = SaleItemsAdapter(saleItems) { position ->
            saleItems.removeAt(position)
            adapter.updateItems(saleItems)
            updateTotals()
        }
        binding.rvSaleItems.layoutManager = LinearLayoutManager(this)
        binding.rvSaleItems.adapter = adapter
    }
    
    private fun setupPaymentMethodSpinner() {
        val paymentMethods = arrayOf("efectivo", "tarjeta", "transferencia")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paymentMethods)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPaymentMethod.adapter = spinnerAdapter
    }
    
    private fun setupListeners() {
        binding.btnAddProduct.setOnClickListener {
            showAddProductDialog()
        }
        
        binding.btnSave.setOnClickListener {
            saveSale()
        }
    }
    
    private fun loadProducts() {
        val token = tokenManager.getToken() ?: return
        
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getProducts("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    products = response.body()!!.data
                }
            } catch (e: Exception) {
                // Error silencioso, se puede continuar
            }
        }
    }
    
    private fun showAddProductDialog() {
        if (products.isEmpty()) {
            Toast.makeText(this, "No hay productos disponibles. Agrega productos primero.", Toast.LENGTH_SHORT).show()
            return
        }
        
        val productNames = products.map { "${it.name} - ${NumberFormat.getCurrencyInstance(Locale("es", "MX")).format(it.price)}" }
        val productArray = productNames.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle("Seleccionar Producto")
            .setItems(productArray) { _, which ->
                val selectedProduct = products[which]
                showQuantityDialog(selectedProduct)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    
    private fun showQuantityDialog(product: Product) {
        val dialogBinding = DialogAddProductBinding.inflate(LayoutInflater.from(this))
        
        dialogBinding.tvProductName.text = product.name
        dialogBinding.tvPrice.text = "Precio: ${NumberFormat.getCurrencyInstance(Locale("es", "MX")).format(product.price)}"
        dialogBinding.tvStock.text = "Stock disponible: ${product.stock}"
        
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setTitle("Agregar Producto")
            .setPositiveButton("Agregar") { _, _ ->
                val quantityText = dialogBinding.etQuantity.text.toString().trim()
                if (quantityText.isEmpty()) {
                    Toast.makeText(this, "Ingresa una cantidad", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                
                val quantity = try {
                    quantityText.toInt()
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Cantidad inválida", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                
                if (quantity <= 0) {
                    Toast.makeText(this, "La cantidad debe ser mayor a 0", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                
                if (quantity > product.stock) {
                    Toast.makeText(this, "No hay suficiente stock. Disponible: ${product.stock}", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                
                val subtotal = product.price * quantity
                val saleItem = SaleItem(
                    productId = product.id ?: "",
                    productName = product.name,
                    quantity = quantity,
                    unitPrice = product.price,
                    subtotal = subtotal
                )
                
                saleItems.add(saleItem)
                adapter.updateItems(saleItems)
                updateTotals()
            }
            .setNegativeButton("Cancelar", null)
            .create()
        
        dialog.show()
    }
    
    private fun updateTotals() {
        val subtotal = saleItems.sumOf { it.subtotal }
        val tax = subtotal * 0.16 // IVA 16%
        val total = subtotal + tax
        
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
        binding.tvSubtotal.text = currencyFormat.format(subtotal)
        binding.tvTotal.text = currencyFormat.format(total)
    }
    
    private fun saveSale() {
        if (saleItems.isEmpty()) {
            Toast.makeText(this, "Agrega al menos un producto a la venta", Toast.LENGTH_SHORT).show()
            return
        }
        
        val customerName = binding.etCustomerName.text.toString().trim()
        val paymentMethod = binding.spinnerPaymentMethod.selectedItem.toString()
        
        val subtotal = saleItems.sumOf { it.subtotal }
        val tax = subtotal * 0.16
        val total = subtotal + tax
        
        val token = tokenManager.getToken() ?: return
        
        binding.btnSave.isEnabled = false
        binding.progressBar.visibility = android.view.View.VISIBLE
        
        val saleRequest = CreateSaleRequest(
            customerName = if (customerName.isNotEmpty()) customerName else null,
            items = saleItems,
            paymentMethod = paymentMethod
        )
        
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.createSale("Bearer $token", saleRequest)
                
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(this@CreateSaleActivity, "Venta creada exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    Toast.makeText(this@CreateSaleActivity, "Error: $errorBody", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CreateSaleActivity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                binding.btnSave.isEnabled = true
                binding.progressBar.visibility = android.view.View.GONE
            }
        }
    }
}

