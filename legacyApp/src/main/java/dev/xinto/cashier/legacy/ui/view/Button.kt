package dev.xinto.cashier.legacy.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatButton
import dev.xinto.cashier.legacy.R

class Button @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.style.IconButton
): AppCompatButton(context, attributeSet, defStyleAttr) {

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        alpha = if (enabled) 1f else 0.5f
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        if (!isEnabled) return false

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                alpha = 0.7f
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                alpha = 1f
                return true
            }
        }
        return false
    }

}