package com.foodorder.order.view.componet

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class UnifiedButton : AppCompatButton {

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}
}

//class ColoredButton : Button {
//
//    private var isRed = true
//
//    constructor(context: Context) : super(context) {}
//
//    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
//
//    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}
//
//    fun setIsRed(isRed: Boolean) {
//        this.isRed = isRed
//        changeBgColor()
//    }
//
//    private fun changeBgColor() {
//        setBackgroundResource(if (isRed) R.drawable.bg_red else R.drawable.bg_green)
//        setText(if (isRed) "Red" else "Green")
//    }
//}