package com.puntoventa.app.ui.sales

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.puntoventa.app.R
import com.puntoventa.app.api.models.Sale
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class SalesAdapter(
    private var sales: List<Sale>
) : RecyclerView.Adapter<SalesAdapter.SaleViewHolder>() {
    
    class SaleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSaleId: TextView = itemView.findViewById(R.id.tvSaleId)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvCustomerName: TextView = itemView.findViewById(R.id.tvCustomerName)
        val tvItems: TextView = itemView.findViewById(R.id.tvItems)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
        val tvPaymentMethod: TextView = itemView.findViewById(R.id.tvPaymentMethod)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sale, parent, false)
        return SaleViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: SaleViewHolder, position: Int) {
        val sale = sales[position]
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
        
        holder.tvSaleId.text = "Venta #${sale.id?.takeLast(6) ?: "N/A"}"
        
        sale.createdAt?.let {
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val date = inputFormat.parse(it)
                holder.tvDate.text = date?.let { outputFormat.format(it) } ?: it
            } catch (e: Exception) {
                holder.tvDate.text = it
            }
        } ?: run {
            holder.tvDate.text = "Sin fecha"
        }
        
        holder.tvCustomerName.text = "Cliente: ${sale.customerName ?: "Cliente general"}"
        holder.tvItems.text = "Items: ${sale.items.size}"
        holder.tvTotal.text = "Total: ${currencyFormat.format(sale.total)}"
        holder.tvPaymentMethod.text = "Método: ${sale.paymentMethod ?: "efectivo"}"
    }
    
    override fun getItemCount(): Int = sales.size
    
    fun updateSales(newSales: List<Sale>) {
        sales = newSales
        notifyDataSetChanged()
    }
}

