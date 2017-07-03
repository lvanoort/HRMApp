package com.lukevanoort.hrm.ui.views.pickdevices

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.FrameLayout
import com.lukevanoort.hrm.R

class PickDevicesView : FrameLayout {
    constructor(context: Context) : super(context) {
        initialize(context)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        initialize(context)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initialize(context)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes){
        initialize(context)
    }

    fun initialize(context: Context) {
        setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
    }
}