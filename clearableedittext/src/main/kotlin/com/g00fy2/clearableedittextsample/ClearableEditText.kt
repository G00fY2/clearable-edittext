package com.g00fy2.clearableedittextsample

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import com.g00fy2.clearableedittextsample.clearableedittext.R

class ClearableEditText : AppCompatEditText {

    private var iconVisible = false
    var clearIconDrawable: Drawable? = null
    var hideClearIconOnFocusLoss = true
    var onClearIconTouchListener: OnTouchListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.editTextStyle)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        clearIconDrawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_clear, null)?.apply {
            setBounds(0, 0, this.intrinsicWidth, this.intrinsicHeight)
        }
        if (!hideClearIconOnFocusLoss) toggleClearDrawable()
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        toggleClearDrawable()
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        toggleClearDrawable()
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event != null && eventInsideClearIcon(event.x)) {
            if (event.action == MotionEvent.ACTION_UP) {
                text?.clear()
            }
            onClearIconTouchListener?.onTouch(this, event)
            return true
        }
        return super.dispatchTouchEvent(event)
    }

    private fun toggleClearDrawable() {
        val show = (hasFocus() || !hideClearIconOnFocusLoss) && !text.isNullOrEmpty()
        if (show != iconVisible && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setCompoundDrawablesRelative(null, null, if (show) clearIconDrawable else null, null)
            iconVisible = show && checkIfIconVisible()
        } else if (show != iconVisible) {
            setCompoundDrawables(null, null, if (show) clearIconDrawable else null, null)
            iconVisible = show && checkIfIconVisible()
        }
    }

    private fun checkIfIconVisible(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && layoutDirection == View.LAYOUT_DIRECTION_RTL) {
            return width - totalPaddingLeft > 0
        } else {
            return width - totalPaddingRight > 0
        }
    }

    private fun eventInsideClearIcon(x: Float): Boolean {
        return if (iconVisible) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                x < totalPaddingLeft
            } else {
                x >= width - totalPaddingRight
            }
        } else {
            false
        }
    }
}