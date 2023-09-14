package com.xinto.cashier.ui.screen.income

import android.os.Bundle
import android.view.View
import com.xinto.cashier.R
import com.xinto.cashier.ui.core.TitleLessDialogFragment
import com.xinto.cashier.ui.view.Button
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