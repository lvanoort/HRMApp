package com.lukevanoort.hrm.ui.views

import android.app.Activity
import android.content.ContextWrapper
import android.view.View
import com.lukevanoort.hrm.ui.ActivityComponent
import com.lukevanoort.hrm.ui.ActivityComponentProvider

fun View.getActivity() : Activity? {
    var context = context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun View.getActivityComponent(): ActivityComponent? {
    val act = this.getActivity()
    return (act as? ActivityComponentProvider)?.provideActivityComponent()
}