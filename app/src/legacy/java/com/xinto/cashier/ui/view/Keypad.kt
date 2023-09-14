package com.xinto.cashier.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.GridLayout
import androidx.core.view.children
import androidx.core.view.marginEnd
import com.xinto.cashier.R

class Keypad @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttrs: Int = 0,
) : FrameLayout(context, attributeSet, defStyleAttrs) {

    fun setOnNumberClickListener(listener: (String) -> Unit) {
        findViewById<GridLayout>(R.id.keypad).children.forEach { view ->
            if (view.id != R.id.keypad_backspace) {
                view.setOnClickListener { button ->
                    listener((button as Button).text.toString())
                }
            }
        }
    }

    fun setOnBackspaceClickListener(listener: () -> Unit) {
        findViewById<IconButton>(R.id.keypad_backspace).setOnClickListener { listener() }
    }

    init {
        inflate(context, R.layout.view_keypad, this)

        context.theme.obtainStyledAttributes(attributeSet, R.styleable.Keypad, 0, 0).apply {
            try {
                val type = getInteger(R.styleable.Keypad_type, 0)
                if (type == 1) {
                    findViewById<Button>(R.id.keypad_decimal).isEnabled = false
                }
            } finally {
                recycle()
            }
        }
    }

}