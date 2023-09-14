package com.xinto.cashier.ui.screen.registry

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xinto.cashier.R
import com.xinto.cashier.domain.model.ParcelableSelectedProduct
import com.xinto.cashier.ui.core.CustomDividerItemDecoration
import com.xinto.cashier.ui.view.IconButton
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistryFragment : Fragment(R.layout.layout_registry) {

    private val viewModel: RegistryViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clearButton = view.findViewById<IconButton>(R.id.registry_action_clear).apply {
            setOnClickListener {
                viewModel.clearProducts()
            }
        }
        val cashButton = view.findViewById<IconButton>(R.id.registry_action_pay_cash).apply {
            setOnClickListener {
                RegistryCashDialogFragment(viewModel.price.value)
                    .show(childFragmentManager, null)
            }
        }
        val cardButton = view.findViewById<IconButton>(R.id.registry_action_pay_card).apply {
            setOnClickListener {
                viewModel.payWithCard()
            }
        }
        val priceText = view.findViewById<TextView>(R.id.registry_price)

        view.findViewById<IconButton>(R.id.registry_refresh).apply {
            setOnClickListener {
                viewModel.refresh()
            }
        }

        val registrySelectableProductsAdapter = RegistrySelectableProductsAdapter(
            onClick = viewModel::selectProduct
        )
        val registrySelectedProductsAdapter = RegistrySelectedProductsAdapter(
            onRemoveClick = viewModel::removeProduct,
            onEditClick = {
                RegistryEditDialogFragment(ParcelableSelectedProduct(it))
                    .show(childFragmentManager, null)
            }
        )

        val registryProductsSuccess = view.findViewById<RecyclerView>(R.id.status_modes)
            .apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = registrySelectableProductsAdapter
                addItemDecoration(CustomDividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        val registryProductsLoading = view.findViewById<FrameLayout>(R.id.registry_products_loading)
        val registryProductsError = view.findViewById<FrameLayout>(R.id.registry_products_error)

        view.findViewById<RecyclerView>(R.id.registry_result).apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = registrySelectedProductsAdapter
            addItemDecoration(CustomDividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        viewModel.state.onEach { state ->
            when (state) {
                is RegistryState.Loading -> {
                    registryProductsLoading.visibility = View.VISIBLE
                    registryProductsSuccess.visibility = View.GONE
                    registryProductsError.visibility = View.GONE
                }
                is RegistryState.Success -> {
                    registrySelectableProductsAdapter.setProducts(state.products)
                    registryProductsSuccess.visibility = View.VISIBLE
                    registryProductsLoading.visibility = View.GONE
                    registryProductsError.visibility = View.GONE
                }
                is RegistryState.Error -> {
                    registryProductsError.visibility = View.VISIBLE
                    registryProductsSuccess.visibility = View.GONE
                    registryProductsLoading.visibility = View.GONE
                }
            }
        }.launchIn(lifecycleScope)

        viewModel.price.onEach {
            priceText.text = resources.getString(R.string.product_price_sum, it.value)
        }.launchIn(lifecycleScope)

        viewModel.selectedProducts.onEach {
            if (it.isNotEmpty()) {
                clearButton.isEnabled = true
                cardButton.isEnabled = true
                cashButton.isEnabled = true
                priceText.alpha = 1f
            } else {
                clearButton.isEnabled = false
                cardButton.isEnabled = false
                cashButton.isEnabled = false
                priceText.alpha = 0.5f
            }
            registrySelectedProductsAdapter.setProducts(it)
        }.launchIn(lifecycleScope)
    }

}