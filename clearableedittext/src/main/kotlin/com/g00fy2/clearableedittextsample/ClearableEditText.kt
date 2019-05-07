package com.g00fy2.clearableedittextsample

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.g00fy2.clearableedittextsample.clearableedittext.R

class ClearableEditText : AppCompatEditText {

    var clearIconDrawable: Drawable? = null
    var onClearIconTouchListener: OnTouchListener? = null
    private var rtl: Boolean = false
    private var iconVisible = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.editTextStyle)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        clearIconDrawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_clear, null)?.apply {
            setBounds(0, 0, this.intrinsicWidth, this.intrinsicHeight)
        }
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        toggleClearDrawable()
    }

    override fun onRtlPropertiesChanged(layoutDirection: Int) {
        super.onRtlPropertiesChanged(layoutDirection)
        rtl = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL
        toggleClearDrawable(true)
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

    private fun toggleClearDrawable(rtlChanged: Boolean = false) {
        val emptyText = text.isNullOrEmpty()
        if (!hasFocus()) {
            setCompoundDrawables(null, null, null, null)
            iconVisible = false
        } else if (emptyText && iconVisible) {
            setCompoundDrawables(null, null, null, null)
            iconVisible = false
        } else if (!emptyText && (rtlChanged || !iconVisible)) {
            setCompoundDrawables(if (rtl) clearIconDrawable else null, null, if (!rtl) clearIconDrawable else null, null)
            iconVisible = true
        }
    }

    private fun eventInsideClearIcon(x: Float): Boolean {
        return if (iconVisible) {
            if (rtl) {
                x < totalPaddingLeft
            } else {
                x >= width - totalPaddingRight
            }
        } else {
            false
        }
    }
}