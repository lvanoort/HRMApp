package com.lukevanoort.hrm.ui

import android.app.Activity
import android.content.Context
import com.lukevanoort.hrm.ui.views.ViewModelModule
import com.lukevanoort.hrm.ui.views.reading.HrmReadingView
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Qualifier
import javax.inject.Scope

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope

@Subcomponent(modules = arrayOf(ActivityModule::class,ViewModelModule::class))
@ActivityScope
interface ActivityComponent {
    fun inject(act: MainActivity)
    fun inject(view: HrmReadingView)

    @Subcomponent.Builder
    interface Builder {
        fun activityModule(module: ActivityModule): Builder
        fun build(): ActivityComponent
    }
}

interface ActivityComponentProvider {
    fun provideActivityComponent(): ActivityComponent
}

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ActivityContext

@Module
class ActivityModule(val activity: Activity) {
    @Provides
    @ActivityScope
    fun provideActivity() : Activity = activity

    @Provides
    @ActivityScope
    @ActivityContext
    fun provideAppContext() : Context = activity
}