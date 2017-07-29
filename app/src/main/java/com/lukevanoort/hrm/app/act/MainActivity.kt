package com.lukevanoort.hrm.app.act

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import com.lukevanoort.hrm.app.R
import com.lukevanoort.hrm.app.getAppComponent
import com.lukevanoort.hrm.ui.ActivityUiComponent
import com.lukevanoort.hrm.ui.ActivityUiComponentProvider
import com.lukevanoort.hrm.ui.ActivityUiModule

class MainActivity : AppCompatActivity(), ActivityUiComponentProvider {

    private lateinit var uiComponent: ActivityUiComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_layout)
        uiComponent = getAppComponent().provideActBuild()
                .activityModule(ActivityUiModule(this))
                .build()

    }

    override fun provideActivityComponent(): ActivityUiComponent = uiComponent

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO)
        }
    }

}


