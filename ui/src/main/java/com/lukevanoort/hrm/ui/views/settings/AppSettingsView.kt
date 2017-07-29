package com.lukevanoort.hrm.ui.views.settings

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

class AppSettingsView: RecyclerView {
    constructor(context: Context) : super(context) {
        initialize(context)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        initialize(context)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle){
        initialize(context)
    }

    private fun initialize(context: Context) {


    }
}