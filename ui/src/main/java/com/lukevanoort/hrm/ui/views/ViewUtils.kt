package com.lukevanoort.hrm.ui.views

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lukevanoort.hrm.ui.ActivityUiComponent
import com.lukevanoort.hrm.ui.ActivityUiComponentProvider

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

fun View.getActivityComponent(): ActivityUiComponent? {
    val act = this.getActivity()
    return (act as? ActivityUiComponentProvider)?.provideActivityComponent()
}

fun ViewGroup.inflateChildren(@LayoutRes layoutRes: Int) : View = this.inflateChildren(layoutRes,true)

fun ViewGroup.inflateChildren(@LayoutRes layoutRes: Int, attachToRoot: Boolean) : View {
    val inflater = LayoutInflater.from(this.context)
    return inflater.inflate(layoutRes,this, attachToRoot)
}

fun Context.tintDrawableResource(@DrawableRes drawable: Int, @ColorRes color: Int) : Drawable {
    val drawable = ContextCompat.getDrawable(this,drawable)
    val color = ContextCompat.getColor(this,color)
    val wrapped = DrawableCompat.wrap(drawable).mutate()
    DrawableCompat.setTint(wrapped,color)
    return wrapped
}

