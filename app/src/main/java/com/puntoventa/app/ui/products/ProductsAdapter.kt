package com.puntoventa.app.ui.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.puntoventa.app.R
import com.puntoventa.app.api.models.Product
import java.text.NumberFormat
import java.util.*

class ProductsAdapter(
    private var products: List<Product>
) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {
    
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvStock: TextView = itemView.findViewById(R.id.tvStock)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
        
        holder.tvProductName.text = product.name
        holder.tvDescription.text = product.description
        holder.tvPrice.text = "Precio: ${currencyFormat.format(product.price)}"
        holder.tvStock.text = "Stock: ${product.stock}"
        holder.tvCategory.text = "Categoría: ${product.category}"
    }
    
    override fun getItemCount(): Int = products.size
    
    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}

