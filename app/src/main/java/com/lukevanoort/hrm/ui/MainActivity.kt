package com.lukevanoort.hrm.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import com.lukevanoort.hrm.R
import com.lukevanoort.hrm.getAppComponent

class MainActivity : AppCompatActivity(), ActivityComponentProvider {

    private lateinit var component: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_layout)
        component = getAppComponent().provideActBuild()
                .activityModule(ActivityModule(this))
                .build()

        component.inject(this)
    }

    override fun provideActivityComponent(): ActivityComponent = component

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO)
        }
    }

}


