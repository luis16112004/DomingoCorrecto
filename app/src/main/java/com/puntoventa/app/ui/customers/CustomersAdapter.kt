package com.puntoventa.app.ui.customers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.puntoventa.app.R
import com.puntoventa.app.api.models.Customer

class CustomersAdapter(
    private var customers: List<Customer>
) : RecyclerView.Adapter<CustomersAdapter.CustomerViewHolder>() {
    
    class CustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCustomerName: TextView = itemView.findViewById(R.id.tvCustomerName)
        val tvPhone: TextView = itemView.findViewById(R.id.tvPhone)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_customer, parent, false)
        return CustomerViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val customer = customers[position]
        
        holder.tvCustomerName.text = customer.name
        holder.tvPhone.text = "Tel: ${customer.phoneNumber}"
        
        if (customer.email != null && customer.email.isNotEmpty()) {
            holder.tvEmail.text = customer.email
            holder.tvEmail.visibility = View.VISIBLE
        } else {
            holder.tvEmail.visibility = View.GONE
        }
        
        val addressParts = mutableListOf<String>()
        customer.address?.let { if (it.isNotEmpty()) addressParts.add(it) }
        customer.city?.let { if (it.isNotEmpty()) addressParts.add(it) }
        customer.state?.let { if (it.isNotEmpty()) addressParts.add(it) }
        
        if (addressParts.isNotEmpty()) {
            holder.tvAddress.text = addressParts.joinToString(", ")
            holder.tvAddress.visibility = View.VISIBLE
        } else {
            holder.tvAddress.visibility = View.GONE
        }
    }
    
    override fun getItemCount(): Int = customers.size
    
    fun updateCustomers(newCustomers: List<Customer>) {
        customers = newCustomers
        notifyDataSetChanged()
    }
}

