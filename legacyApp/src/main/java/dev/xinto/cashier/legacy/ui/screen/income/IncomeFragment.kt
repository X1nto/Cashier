package dev.xinto.cashier.legacy.ui.screen.income

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.xinto.cashier.common.domain.model.StatusMode
import dev.xinto.cashier.common.ui.screen.income.IncomeViewModel
import dev.xinto.cashier.legacy.R
import dev.xinto.cashier.legacy.ui.core.CustomDividerItemDecoration
import dev.xinto.cashier.legacy.ui.view.IconButton
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import dev.xinto.cashier.common.R as CR

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
                    background = null
                }
                addView(radioButton)
            }
        }
        val modeIncome = view.findViewById<TextView>(R.id.income_mode_income)
        val fullIncome = view.findViewById<TextView>(R.id.income_full_income)

        viewModel.statusMode
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
            .onEach {
                val tag = incomeModes.findViewWithTag<RadioButton>(it)
                incomeModes.check(tag.id)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.products
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
            .onEach {
                incomeAdapter.setProducts(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.pricePerMode
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED).onEach {
                modeIncome.text = resources.getString(CR.string.product_price_sum, it.value)
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.price.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
            .onEach {
                fullIncome.text = resources.getString(CR.string.product_price_sum, it.value)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun getStringForStatusMode(statusMode: StatusMode): String {
        val string = when (statusMode) {
            StatusMode.CardMeal -> CR.string.income_meal_card
            StatusMode.CashMeal -> CR.string.income_meal_cash
            StatusMode.CardDrink -> CR.string.income_drink_card
            StatusMode.CashDrink -> CR.string.income_drink_cash
        }
        return resources.getString(string)
    }

}