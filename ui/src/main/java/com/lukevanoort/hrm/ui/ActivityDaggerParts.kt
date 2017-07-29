package com.lukevanoort.hrm.ui

import android.app.Activity
import android.content.Context
import com.lukevanoort.hrm.dagger.ActivityScope
import com.lukevanoort.hrm.ui.views.ViewModelModule
import com.lukevanoort.hrm.ui.views.reading.HrmReadingView
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Qualifier

@Subcomponent(modules = arrayOf(ActivityUiModule::class, ViewModelModule::class))
@ActivityScope
interface ActivityUiComponent {
    fun inject(view: HrmReadingView)

    @Subcomponent.Builder
    interface Builder {
        fun activityModule(uiModule: ActivityUiModule): Builder
        fun build(): ActivityUiComponent
    }
}

interface ActivityUiComponentProvider {
    fun provideActivityComponent(): ActivityUiComponent
}

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ActivityContext

@Module
class ActivityUiModule(val activity: Activity) {
    @Provides
    @ActivityScope
    fun provideActivity() : Activity = activity

    @Provides
    @ActivityScope
    @ActivityContext
    fun provideAppContext() : Context = activity
}