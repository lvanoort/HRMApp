package com.lukevanoort.hrm.ui.views.rootview

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.util.AttributeSet
import android.view.View
import com.lukevanoort.hrm.R
import com.lukevanoort.hrm.ui.views.inflateChildren

class RootView : ConstraintLayout {

    constructor(context: Context) : super(context) {
        init(context)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun init(context: Context) {
        inflateChildren(R.layout.root_view_layout)
    }
}
