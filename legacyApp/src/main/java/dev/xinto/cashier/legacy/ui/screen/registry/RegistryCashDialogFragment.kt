package dev.xinto.cashier.legacy.ui.screen.registry

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dev.xinto.cashier.common.domain.model.Price
import dev.xinto.cashier.common.ui.screen.registry.RegistryCashViewModel
import dev.xinto.cashier.legacy.R
import dev.xinto.cashier.legacy.ui.core.TitleLessDialogFragment
import dev.xinto.cashier.legacy.ui.view.Keypad
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import dev.xinto.cashier.common.R as CR

class RegistryCashDialogFragment : TitleLessDialogFragment(R.layout.dialog_cash) {

    private val viewModel: RegistryCashViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val display = view.findViewById<EditText>(R.id.cash_display)
        view.findViewById<Keypad>(R.id.cash_keypad).apply {
            setOnBackspaceClickListener(viewModel::popDigit)
            setOnNumberClickListener(viewModel::appendDigit)
        }
        val change = view.findViewById<TextView>(R.id.cash_change)
        view.findViewById<Button>(R.id.cash_action_cancel).apply {
            setOnClickListener {
                dismiss()
            }
        }
        val payButton = view.findViewById<Button>(R.id.cash_action_pay).apply {
            setOnClickListener {
                viewModel.pay()
                dismiss()
            }
        }

        viewModel.change
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
            .onEach {
                payButton.isEnabled = it != null
                change.isInvisible = it == null

                if (it != null) {
                    change.text = resources.getString(CR.string.cash_change, it)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.cash
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
            .onEach {
                display.setText(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    companion object {

        operator fun invoke(price: Price): RegistryCashDialogFragment {
            return RegistryCashDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(RegistryCashViewModel.KEY_PRICE, price)
                }
            }
        }
    }
}