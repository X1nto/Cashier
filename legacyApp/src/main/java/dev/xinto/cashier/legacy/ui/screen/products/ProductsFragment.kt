package dev.xinto.cashier.legacy.ui.screen.products

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.xinto.cashier.common.ui.screen.products.ProductsState
import dev.xinto.cashier.common.ui.screen.products.ProductsViewModel
import dev.xinto.cashier.legacy.R
import dev.xinto.cashier.legacy.ui.core.CustomDividerItemDecoration
import dev.xinto.cashier.legacy.ui.view.IconButton
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductsFragment : Fragment(R.layout.layout_products) {

    private val viewModel: ProductsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productItemsAdapter = ProductsItemsAdapter(
            onEditClick = {
                ProductsProductDialogFragment(it)
                    .show(childFragmentManager, null)
            },
            onRemoveClick = viewModel::removeItem,
            onRestoreClick = viewModel::restoreItem
        )
        val itemsSuccess = view.findViewById<RecyclerView>(R.id.products_items_success).apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = productItemsAdapter
            addItemDecoration(CustomDividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            addItemDecoration(CustomDividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
        }
        val itemsLoading = view.findViewById<TextView>(R.id.products_items_loading)
        val itemsError = view.findViewById<TextView>(R.id.products_items_error)

        val saveButton = view.findViewById<IconButton>(R.id.products_save)
            .apply {
                setOnClickListener {
                }
            }
        view.findViewById<IconButton>(R.id.products_add)
            .apply {
                setOnClickListener {
                    ProductsProductDialogFragment()
                        .show(childFragmentManager, null)
                }
            }

        viewModel.saveEnabled
            .onEach {
                saveButton.isEnabled = it
            }.launchIn(lifecycleScope)

        viewModel.state
            .onEach {
                itemsLoading.isVisible = it is ProductsState.Loading
                itemsSuccess.isVisible = it is ProductsState.Success
                itemsError.isVisible = it is ProductsState.Error

                if (it is ProductsState.Success) {
                    productItemsAdapter.setProducts(it.products)
                }
            }.launchIn(lifecycleScope)
    }

}