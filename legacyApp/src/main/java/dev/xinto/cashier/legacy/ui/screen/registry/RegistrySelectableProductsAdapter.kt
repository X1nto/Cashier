package dev.xinto.cashier.legacy.ui.screen.registry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.xinto.cashier.legacy.R
import dev.xinto.cashier.common.R as CR
import dev.xinto.cashier.common.domain.model.SelectableProduct
import dev.xinto.cashier.legacy.ui.view.IconButton

class RegistrySelectableProductsAdapter(
    private inline val onClick: (SelectableProduct) -> Unit
) : RecyclerView.Adapter<RegistrySelectableProductsAdapter.ProductViewHolder>() {

    private val items = mutableListOf<SelectableProduct>()

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val addButton = view.findViewById<IconButton>(R.id.product_selectable_action_add)
        val productName = view.findViewById<TextView>(R.id.product_selectable_name)
        val productPrice = view.findViewById<TextView>(R.id.product_selectable_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.widget_product_selectable, parent, false)
        return ProductViewHolder(layout)
    }

    fun setProducts(products: List<SelectableProduct>) {
        items.clear()
        items.addAll(products)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = items[position]
        holder.productName.text = product.name
        holder.productPrice.text = holder.productName.context.getString(CR.string.product_price_single, product.price.value)
        holder.addButton.setOnClickListener {
            onClick(product)
        }
    }

}