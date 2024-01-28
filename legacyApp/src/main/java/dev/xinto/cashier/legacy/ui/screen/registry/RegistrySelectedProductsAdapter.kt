package dev.xinto.cashier.legacy.ui.screen.registry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.xinto.cashier.legacy.R
import dev.xinto.cashier.common.R as CR
import dev.xinto.cashier.common.domain.model.SelectedProduct
import dev.xinto.cashier.legacy.ui.view.IconButton

class RegistrySelectedProductsAdapter(
    private inline val onEditClick: (SelectedProduct) -> Unit,
    private inline val onRemoveClick: (SelectedProduct) -> Unit
) : RecyclerView.Adapter<RegistrySelectedProductsAdapter.SelectedProductsViewHolder>() {

    private val items = mutableListOf<SelectedProduct>()

    inner class SelectedProductsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val editButton = view.findViewById<IconButton>(R.id.product_selected_action_edit)
        val removeButton = view.findViewById<IconButton>(R.id.product_selected_action_remove)
        val productName = view.findViewById<TextView>(R.id.product_selected_name)
        val productCount = view.findViewById<TextView>(R.id.product_selected_count)
        val productPrice = view.findViewById<TextView>(R.id.product_selected_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedProductsViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.widget_product_selected, parent, false)
        return SelectedProductsViewHolder(layout)
    }

    fun setProducts(products: List<SelectedProduct>) {
        items.clear()
        items.addAll(products)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SelectedProductsViewHolder, position: Int) {
        val product = items[position]
        holder.editButton.setOnClickListener {
            onEditClick(product)
        }
        holder.removeButton.setOnClickListener {
            onRemoveClick(product)
        }
        holder.productName.text = product.name
        holder.productCount.text = holder.productCount.context.getString(CR.string.product_amount, product.countAsString)
        holder.productPrice.text = holder.productPrice.context.getString(CR.string.product_price_sum, product.price.value)
    }

}