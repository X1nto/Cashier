package dev.xinto.cashier.legacy.ui.screen.income

import android.os.Bundle
import android.view.View
import dev.xinto.cashier.common.ui.screen.income.IncomeClearViewModel
import dev.xinto.cashier.legacy.R
import dev.xinto.cashier.legacy.ui.core.TitleLessDialogFragment
import dev.xinto.cashier.legacy.ui.view.Button
import org.koin.androidx.viewmodel.ext.android.viewModel

class IncomeClearDialogFragment : TitleLessDialogFragment(R.layout.dialog_clear) {

    private val viewModel: IncomeClearViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<Button>(R.id.clear_action_cancel)
            .setOnClickListener {
                dismiss()
            }

        view.findViewById<Button>(R.id.clear_action_delete)
            .setOnClickListener {
                viewModel.clearDatabase()
                dismiss()
            }
    }

}