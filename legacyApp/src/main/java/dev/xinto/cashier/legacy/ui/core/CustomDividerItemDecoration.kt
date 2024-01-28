package dev.xinto.cashier.legacy.ui.core

import android.content.Context
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import dev.xinto.cashier.legacy.R

class CustomDividerItemDecoration(context: Context, orientation: Int) : DividerItemDecoration(context, orientation) {

    init {
        setDrawable(ResourcesCompat.getDrawable(context.resources, R.drawable.divider, context.theme)!!)
    }

}