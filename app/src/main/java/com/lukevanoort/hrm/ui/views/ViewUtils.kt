package com.lukevanoort.hrm.ui.views

import android.app.Activity
import android.content.ContextWrapper
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

fun ViewGroup.inflateChildren(@LayoutRes layoutRes: Int) : View = this.inflateChildren(layoutRes,true)

fun ViewGroup.inflateChildren(@LayoutRes layoutRes: Int, attachToRoot: Boolean) : View {
    val inflater = LayoutInflater.from(this.context)
    return inflater.inflate(layoutRes,this, attachToRoot)
}

