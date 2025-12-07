package com.puntoventa.app.ui.sales

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.puntoventa.app.R
import com.puntoventa.app.api.models.SaleItem
import java.text.NumberFormat
import java.util.*

class SaleItemsAdapter(
    private var items: MutableList<SaleItem>,
    private val onRemoveClick: (Int) -> Unit
) : RecyclerView.Adapter<SaleItemsAdapter.SaleItemViewHolder>() {
    
    class SaleItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        val tvSubtotal: TextView = itemView.findViewById(R.id.tvSubtotal)
        val btnRemove: Button = itemView.findViewById(R.id.btnRemove)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sale_product, parent, false)
        return SaleItemViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: SaleItemViewHolder, position: Int) {
        val item = items[position]
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
        
        holder.tvProductName.text = item.productName
        holder.tvQuantity.text = "Cantidad: ${item.quantity}"
        holder.tvSubtotal.text = "Subtotal: ${currencyFormat.format(item.subtotal)}"
        
        holder.btnRemove.setOnClickListener {
            onRemoveClick(position)
        }
    }
    
    override fun getItemCount(): Int = items.size
    
    fun updateItems(newItems: MutableList<SaleItem>) {
        items = newItems
        notifyDataSetChanged()
    }
    
    fun addItem(item: SaleItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }
    
    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}

