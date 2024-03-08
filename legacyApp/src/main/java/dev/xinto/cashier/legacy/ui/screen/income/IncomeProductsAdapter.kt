package dev.xinto.cashier.legacy.ui.screen.income

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.xinto.cashier.common.domain.model.StatusProduct
import dev.xinto.cashier.legacy.R
import dev.xinto.cashier.common.R as CR

class IncomeProductsAdapter :
    RecyclerView.Adapter<IncomeProductsAdapter.ViewHolder>() {

    private val items = mutableListOf<StatusProduct>()

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val productName = view.findViewById<TextView>(R.id.product_income_name)
        val productCount = view.findViewById<TextView>(R.id.product_income_count)
        val productPrice = view.findViewById<TextView>(R.id.product_income_price)

        val context: Context
            get() = view.context
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.widget_product_income, parent, false)
        return ViewHolder(layout)
    }

    fun setProducts(products: List<StatusProduct>) {
        items.clear()
        items.addAll(products)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val incomeProduct = items[position]
        holder.productName.text = incomeProduct.name
        holder.productCount.text = holder.context.getString(CR.string.product_amount, incomeProduct.countAsString)
        holder.productPrice.text = holder.context.getString(CR.string.product_price_sum, incomeProduct.price.toString())
    }

}