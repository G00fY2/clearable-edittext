package com.g00fy2.clearableedittextsample

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.R
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat

class ClearableEditText : AppCompatEditText {

    var clearIconDrawable: Drawable? = null
    var onClearIconTouchListener: OnTouchListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.attr.editTextStyle)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        clearIconDrawable = ResourcesCompat.getDrawable(resources, R.drawable.abc_ic_clear_material, null)?.apply {
            setBounds(0, 0, this.intrinsicWidth, this.intrinsicHeight)
        }
        toggleClearDrawable(text)
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        toggleClearDrawable(text)
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event != null && event.x >= width - totalPaddingRight) {
            if (event.action == MotionEvent.ACTION_UP) {
                text?.clear()
            }
            onClearIconTouchListener?.onTouch(this, event)
            return true
        }
        return super.dispatchTouchEvent(event)
    }

    private fun toggleClearDrawable(text: CharSequence?) {
        setCompoundDrawables(null, null, if (text.isNullOrEmpty()) null else clearIconDrawable, null)
    }
}