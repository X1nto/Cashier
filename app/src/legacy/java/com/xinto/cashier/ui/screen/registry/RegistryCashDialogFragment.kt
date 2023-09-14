package com.xinto.cashier.ui.screen.registry

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.xinto.cashier.R
import com.xinto.cashier.domain.model.Price
import com.xinto.cashier.ui.core.TitleLessDialogFragment
import com.xinto.cashier.ui.view.Keypad
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistryCashDialogFragment private constructor() : TitleLessDialogFragment(R.layout.dialog_cash) {

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

        viewModel.change.onEach {
            if (it == null) {
                payButton.isEnabled = false
                change.visibility = View.INVISIBLE
            } else {
                payButton.isEnabled = true
                change.visibility = View.VISIBLE
                change.text = resources.getString(R.string.cash_change, it)
            }
        }.launchIn(lifecycleScope)

        viewModel.cash.onEach {
            display.setText(it)
        }.launchIn(lifecycleScope)
    }

    companion object {

        operator fun invoke(price: Price): RegistryCashDialogFragment {
            return RegistryCashDialogFragment().apply {
                arguments = Bundle().apply {
                    putDouble(RegistryCashViewModel.KEY_PRICE, price.value)
                }
            }
        }
    }
}