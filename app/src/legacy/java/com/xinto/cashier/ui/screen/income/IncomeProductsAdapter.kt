package com.xinto.cashier.ui.screen.income

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xinto.cashier.R
import com.xinto.cashier.domain.model.StatusProduct

class IncomeProductsAdapter : RecyclerView.Adapter<IncomeProductsAdapter.IncomeProductViewHolder>() {

    private val items = mutableListOf<StatusProduct>()

    inner class IncomeProductViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val productName = view.findViewById<TextView>(R.id.product_income_name)
        val productCount = view.findViewById<TextView>(R.id.product_income_count)
        val productPrice = view.findViewById<TextView>(R.id.product_income_price)
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeProductViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.widget_product_income, parent, false)
        return IncomeProductViewHolder(layout)
    }

    fun setProducts(products: List<StatusProduct>) {
        items.clear()
        items.addAll(products)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: IncomeProductViewHolder, position: Int) {
        val incomeProduct = items[position]
        holder.productName.text = incomeProduct.name
        holder.productCount.text = holder.productCount.context.getString(R.string.product_amount, incomeProduct.countAsString)
        holder.productPrice.text = holder.productPrice.context.getString(R.string.price, incomeProduct.price.value)
    }

}