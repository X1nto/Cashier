package dev.xinto.cashier.legacy.ui.screen.registry

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.xinto.cashier.common.ui.screen.registry.RegistryState
import dev.xinto.cashier.common.ui.screen.registry.RegistryViewModel
import dev.xinto.cashier.legacy.R
import dev.xinto.cashier.legacy.ui.core.CustomDividerItemDecoration
import dev.xinto.cashier.legacy.ui.view.IconButton
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import dev.xinto.cashier.common.R as CR

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
                RegistryEditDialogFragment(it)
                    .show(childFragmentManager, null)
            }
        )

        val registryProductsSuccess = view.findViewById<RecyclerView>(R.id.status_modes)
            .apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = registrySelectableProductsAdapter
                addItemDecoration(
                    CustomDividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
        val registryProductsLoading = view.findViewById<FrameLayout>(R.id.registry_products_loading)
        val registryProductsError = view.findViewById<FrameLayout>(R.id.registry_products_error)

        view.findViewById<RecyclerView>(R.id.registry_result).apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = registrySelectedProductsAdapter
            addItemDecoration(CustomDividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
            .onEach { state ->
                registryProductsLoading.isVisible = state is RegistryState.Loading
                registryProductsSuccess.isVisible = state is RegistryState.Success
                registryProductsError.isVisible = state is RegistryState.Error

                if (state is RegistryState.Success) {
                    registrySelectableProductsAdapter.setProducts(state.products)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.price
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
            .onEach {
                priceText.text = resources.getString(CR.string.product_price_sum, it.value)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.selectedProducts
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
            .onEach {
                clearButton.isEnabled = it.isNotEmpty()
                cardButton.isEnabled = it.isNotEmpty()
                cashButton.isEnabled = it.isNotEmpty()

                priceText.alpha = if (it.isNotEmpty()) 1f else 0.5f

                registrySelectedProductsAdapter.setProducts(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

}