package com.xinto.cashier.ui.screen.income

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xinto.cashier.R
import com.xinto.cashier.domain.model.StatusMode
import com.xinto.cashier.ui.core.CustomDividerItemDecoration
import com.xinto.cashier.ui.view.IconButton
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel


class IncomeFragment : Fragment(R.layout.layout_income) {

    private val viewModel: IncomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val incomeAdapter = IncomeProductsAdapter()
        view.findViewById<RecyclerView>(R.id.income_result).apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = incomeAdapter
            addItemDecoration(CustomDividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        view.findViewById<IconButton>(R.id.income_clear)
            .setOnClickListener {
                IncomeClearDialogFragment().show(childFragmentManager, null)
            }
        val incomeModes = view.findViewById<RadioGroup>(R.id.income_modes).apply {
            val margin = (16 * resources.displayMetrics.density + 0.5f).toInt()
            StatusMode.entries.forEach { statusMode ->
                val radioButton = RadioButton(context).apply {
                    layoutDirection = View.LAYOUT_DIRECTION_RTL
                    textSize = 22f
                    layoutParams = RadioGroup.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1.0f
                    ).apply {
                        setMargins(margin)
                    }
                    tag = statusMode
                    text = getStringForStatusMode(statusMode)
                    setOnClickListener {
                        viewModel.updateStatusMode(statusMode)
                    }
                }
                addView(radioButton)
            }
        }
        val modeIncome = view.findViewById<TextView>(R.id.income_mode_income)
        val fullIncome = view.findViewById<TextView>(R.id.income_full_income)

        viewModel.statusMode.onEach {
            val tag = incomeModes.findViewWithTag<RadioButton>(it)
            incomeModes.check(tag.id)
        }.launchIn(lifecycleScope)

        viewModel.products.onEach {
            incomeAdapter.setProducts(it)
        }.launchIn(lifecycleScope)

        viewModel.pricePerMode.onEach {
            modeIncome.text = resources.getString(R.string.product_price_sum, it.value)
        }.launchIn(lifecycleScope)

        viewModel.price.onEach {
            fullIncome.text = resources.getString(R.string.product_price_sum, it.value)
        }.launchIn(lifecycleScope)
    }

    private fun getStringForStatusMode(statusMode: StatusMode): String {
        val string = when (statusMode) {
            StatusMode.CardMeal -> R.string.income_meal_card
            StatusMode.CashMeal -> R.string.income_meal_cash
            StatusMode.CardDrink -> R.string.income_drink_card
            StatusMode.CashDrink -> R.string.income_drink_cash
        }
        return resources.getString(string)
    }

}