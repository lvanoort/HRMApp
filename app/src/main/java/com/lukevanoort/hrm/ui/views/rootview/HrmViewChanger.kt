package com.lukevanoort.hrm.ui.views.rootview

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.lukevanoort.hrm.R
import com.lukevanoort.hrm.ui.views.inflateChildren
import com.lukevanoort.hrm.ui.views.pickdevices.PickDevicesView
import com.lukevanoort.hrm.ui.views.reading.HrmReadingView
import com.lukevanoort.hrm.ui.views.settings.AppSettingsView
import kotlinx.android.synthetic.main.root_view_layout.view.*

class RootView : ConstraintLayout {
    var currentIdx: Int = OptionAadapter.IDX_DEVICES

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
        vp_main_view_pager.adapter = OptionAadapter()
        bnv_main_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_pick_device -> {
                    currentIdx = OptionAadapter.IDX_DEVICES
                    vp_main_view_pager.currentItem = currentIdx
                    true
                }
                R.id.nav_see_reading -> {
                    currentIdx = OptionAadapter.IDX_READING
                    vp_main_view_pager.currentItem = currentIdx
                    true
                }
                R.id.nav_app_settings -> {
                    currentIdx = OptionAadapter.IDX_SETTINGS
                    vp_main_view_pager.currentItem = currentIdx
                    true
                }
                else -> false
            }
        }
        vp_main_view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentIdx = position
                reset()
            }
        })
    }

    private fun reset() {
        when(currentIdx) {
            OptionAadapter.IDX_DEVICES -> {
                vp_main_view_pager.currentItem = OptionAadapter.IDX_DEVICES
                bnv_main_navigation.selectedItemId = R.id.nav_pick_device
            }
            OptionAadapter.IDX_READING -> {
                vp_main_view_pager.currentItem = OptionAadapter.IDX_READING
                bnv_main_navigation.selectedItemId = R.id.nav_see_reading
            }
            OptionAadapter.IDX_SETTINGS -> {
                vp_main_view_pager.currentItem = OptionAadapter.IDX_SETTINGS
                bnv_main_navigation.selectedItemId = R.id.nav_see_reading
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val state = RootSavedState(superState)
        state.savedIdx = currentIdx
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if(state is RootSavedState) {
            super.onRestoreInstanceState(state.superState)
            currentIdx = state.savedIdx
            reset()
        } else {
            super.onRestoreInstanceState(state)
        }
    }


}

private class RootSavedState : View.BaseSavedState {
    var savedIdx: Int  = OptionAadapter.IDX_DEVICES

    constructor(superState: Parcelable): super(superState)

    private constructor(inbound: Parcel) : super(inbound){
        savedIdx = inbound.readInt();
    }

    override fun writeToParcel(out: Parcel , flags: Int) {
        super.writeToParcel(out, flags)
        out.writeInt(savedIdx)
    }

    companion object {
        val CREATOR = object : Parcelable.Creator<RootSavedState> {

            override fun createFromParcel(inbound: Parcel) : RootSavedState  {
                return RootSavedState(inbound)
            }

            override fun newArray(size: Int): Array<RootSavedState?>  {
                return arrayOfNulls<RootSavedState>(size)
            }
        };
    }
}

private class OptionAadapter: PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        when(position) {
            IDX_DEVICES -> {
                val view = PickDevicesView(container.context)
                view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
                container.addView(view)
                return view
            }
            IDX_READING -> {
                val view = HrmReadingView(container.context)
                view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
                container.addView(view)
                return view
            }
            IDX_SETTINGS -> {
                val view = AppSettingsView(container.context)
                view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
                container.addView(view)
                return view
            }
            else -> {
                throw IllegalArgumentException("Invalid index")
            }
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any?) {
        container.removeView(`object` as? View)
    }

    override fun isViewFromObject(view: View, `object`: Any?): Boolean = view == `object`

    override fun getCount(): Int = 3

    companion object {
        val IDX_DEVICES = 0
        val IDX_READING = 1
        val IDX_SETTINGS = 2
    }
}
