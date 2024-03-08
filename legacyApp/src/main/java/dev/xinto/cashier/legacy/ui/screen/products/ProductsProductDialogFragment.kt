package dev.xinto.cashier.legacy.ui.screen.products

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import dev.xinto.cashier.common.domain.model.ProductItem
import dev.xinto.cashier.common.ui.screen.products.ProductsProductViewModel
import dev.xinto.cashier.common.ui.screen.registry.RegistryEditViewModel
import dev.xinto.cashier.legacy.R
import dev.xinto.cashier.legacy.ui.view.Button
import dev.xinto.cashier.legacy.ui.view.Keypad
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductsProductDialogFragment : DialogFragment(R.layout.dialog_product) {

    private val viewModel: ProductsProductViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameField = view.findViewById<AppCompatEditText>(R.id.products_product_name).apply {
            addTextChangedListener {
                viewModel.updateName(it.toString())
            }
        }
        val priceField = view.findViewById<AppCompatEditText>(R.id.products_product_price)
        view.findViewById<Keypad>(R.id.products_product_keypad).apply {
            setOnBackspaceClickListener(viewModel::popDigit)
            setOnNumberClickListener(viewModel::appendDigit)
        }

        view.findViewById<Button>(R.id.products_product_action_cancel).apply {
            setOnClickListener {
                dismiss()
            }
        }
        val saveButton = view.findViewById<Button>(R.id.products_product_action_save).apply {
            setOnClickListener {
                viewModel.save()
                dismiss()
            }
        }

        viewModel.price
            .onEach {
                priceField.setText(it)
            }.launchIn(lifecycleScope)

        viewModel.name
            .onEach {
                if (nameField.text.toString() != it) {
                    nameField.setText(it)
                }
            }.launchIn(lifecycleScope)

        viewModel.canSave
            .onEach {
                saveButton.isEnabled = it
            }.launchIn(lifecycleScope)
    }

    companion object {
        operator fun invoke(product: ProductItem): ProductsProductDialogFragment {
            return ProductsProductDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(RegistryEditViewModel.KEY_PRODUCT, product)
                }
            }
        }
    }

}