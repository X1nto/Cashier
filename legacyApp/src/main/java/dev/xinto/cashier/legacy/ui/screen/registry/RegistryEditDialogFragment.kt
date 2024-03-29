package dev.xinto.cashier.legacy.ui.screen.registry

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dev.xinto.cashier.common.domain.model.SelectedProduct
import dev.xinto.cashier.common.ui.screen.registry.RegistryEditViewModel
import dev.xinto.cashier.legacy.R
import dev.xinto.cashier.legacy.ui.core.TitleLessDialogFragment
import dev.xinto.cashier.legacy.ui.view.IconButton
import dev.xinto.cashier.legacy.ui.view.Keypad
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import dev.xinto.cashier.common.R as CR

class RegistryEditDialogFragment : TitleLessDialogFragment(R.layout.dialog_edit) {

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

        viewModel.product
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
            .onEach {
                saveButton.isEnabled = it != null
                incrementButton.isEnabled = it != null
                decrementButton.isEnabled = it != null

                if (it == null) return@onEach

                price.text = resources.getString(CR.string.product_price_sum, it.price.toString())
                title.text = it.name
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.count
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
            .onEach {
                display.setText(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    companion object {

        operator fun invoke(product: SelectedProduct): RegistryEditDialogFragment {
            return RegistryEditDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(RegistryEditViewModel.KEY_PRODUCT, product)
                }
            }
        }
    }
}