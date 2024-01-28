package dev.xinto.cashier.legacy.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageButton
import dev.xinto.cashier.legacy.R

class Tab @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.style.Tab
) : AppCompatImageButton(context, attributeSet, defStyleAttr) {

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        alpha = if (selected) 1f else 0.5f
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                alpha = 0.7f
                return true
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                alpha = if (isSelected) 1f else 0.5f
                return true
            }
        }
        return false
    }

}