package dev.xinto.cashier.legacy.ui.screen.products

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import dev.xinto.cashier.common.domain.model.ProductItem
import dev.xinto.cashier.common.domain.model.ProductItemStatus
import dev.xinto.cashier.legacy.R
import dev.xinto.cashier.legacy.ui.view.IconButton
import dev.xinto.cashier.common.R as CR

class ProductsItemsAdapter(
    private inline val onEditClick: (ProductItem) -> Unit,
    private inline val onRemoveClick: (String) -> Unit,
    private inline val onRestoreClick: (String) -> Unit,
) : RecyclerView.Adapter<ProductsItemsAdapter.ViewHolder>() {

    private val items = mutableListOf<ProductItem>()

    inner class ViewHolder(val background: View) : RecyclerView.ViewHolder(background) {
        val productName = background.findViewById<TextView>(R.id.product_name)
        val productPrice = background.findViewById<TextView>(R.id.product_price)
        val editAndRemoveButtonsContainer = background.findViewById<LinearLayout>(R.id.product_edit_remove_container)
        val editButton = background.findViewById<IconButton>(R.id.product_action_edit)
        val removeButton = background.findViewById<IconButton>(R.id.product_action_remove)
        val restoreButton = background.findViewById<IconButton>(R.id.product_action_restore)

        val context: Context
            get() = background.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.widget_product, parent, false)
        return ViewHolder(layout)
    }

    fun setProducts(products: List<ProductItem>) {
        items.clear()
        items.addAll(products)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = items[position]

        holder.productName.text = product.name
        holder.productPrice.text = holder.context.getString(CR.string.product_price_single, product.price.toString())
        val colorRes = when (product.status) {
            ProductItemStatus.New -> R.color.green
            ProductItemStatus.Edited -> R.color.blue
            ProductItemStatus.Removed -> R.color.red
            ProductItemStatus.Existing -> null
        }
        val color = if (colorRes != null) {
            ColorUtils.setAlphaComponent(ContextCompat.getColor(holder.context, colorRes), 50)
        } else {
            ContextCompat.getColor(holder.context, android.R.color.transparent)
        }
        holder.background.setBackgroundColor(color)

        holder.editAndRemoveButtonsContainer.isVisible = product.status == ProductItemStatus.Existing
        holder.restoreButton.isVisible = product.status != ProductItemStatus.Existing

        holder.editButton.setOnClickListener {
            onEditClick(product)
        }

        holder.removeButton.setOnClickListener {
            onRemoveClick(product.id)
        }

        holder.restoreButton.setOnClickListener {
            onRestoreClick(product.id)
        }

    }


}