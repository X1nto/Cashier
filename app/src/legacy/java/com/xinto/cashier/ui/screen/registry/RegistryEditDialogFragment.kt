package com.xinto.cashier.ui.screen.registry

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.xinto.cashier.R
import com.xinto.cashier.domain.model.ParcelableSelectedProduct
import com.xinto.cashier.ui.core.TitleLessDialogFragment
import com.xinto.cashier.ui.view.IconButton
import com.xinto.cashier.ui.view.Keypad
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistryEditDialogFragment private constructor() : TitleLessDialogFragment(R.layout.dialog_edit) {

    private val viewModel: RegistryEditViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = view.findViewById<TextView>(R.id.edit_title)
        val display = view.findViewById<EditText>(R.id.edit_display)
        view.findViewById<Keypad>(R.id.edit_keypad).apply {
            setOnBackspaceClickListener(viewModel::popDigit)
            setOnNumberClickListener(viewModel::appendDigit)
        }
        val incrementButton = view.findViewById<IconButton>(R.id.edit_action_increment).apply {
            setOnClickListener {
                viewModel.increment()
            }
        }
        val decrementButton = view.findViewById<IconButton>(R.id.edit_action_decrement).apply {
            setOnClickListener {
                viewModel.decrement()
            }
        }
        val price = view.findViewById<TextView>(R.id.edit_price)
        view.findViewById<Button>(R.id.edit_action_cancel).apply {
            setOnClickListener {
                dismiss()
            }
        }
        val saveButton = view.findViewById<Button>(R.id.edit_action_save).apply {
            setOnClickListener {
                viewModel.save()
                dismiss()
            }
        }

        viewModel.product.onEach {
            if (it == null) {
                saveButton.isEnabled = false
                incrementButton.isEnabled = false
                decrementButton.isEnabled = false
                price.visibility = View.INVISIBLE
            } else {
                if (title.text != it.name) {
                    title.text = it.name
                }
                saveButton.isEnabled = true
                incrementButton.isEnabled = true
                decrementButton.isEnabled = true
                price.visibility = View.VISIBLE
                price.text = resources.getString(R.string.product_price_sum, it.price.value)
            }
        }.launchIn(lifecycleScope)

        viewModel.count.onEach {
            display.setText(it)
        }.launchIn(lifecycleScope)
    }

    companion object {

        operator fun invoke(product: ParcelableSelectedProduct): RegistryEditDialogFragment {
            return RegistryEditDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(RegistryEditViewModel.KEY_PRODUCT, product)
                }
            }
        }
    }
}