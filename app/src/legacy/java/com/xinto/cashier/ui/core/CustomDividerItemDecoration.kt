package com.xinto.cashier.ui.core

import android.content.Context
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.xinto.cashier.R

class CustomDividerItemDecoration(context: Context, orientation: Int) : DividerItemDecoration(context, orientation) {

    init {
        setDrawable(ResourcesCompat.getDrawable(context.resources, R.drawable.divider, context.theme)!!)
    }

}