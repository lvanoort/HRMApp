package com.lukevanoort.hrm

import android.app.Application
import android.content.Context
import android.os.Looper
import com.lukevanoort.hrm.business.state.StateModule
import com.lukevanoort.hrm.ui.ActivityComponent
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Qualifier

///*
@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class AppContext

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class OnUiThread

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class OnStateThread

class HrmApplication : Application() {
    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    fun provideAppComponent() : AppComponent = appComponent;
}

inline fun Context.getAppComponent(): AppComponent = (this.applicationContext as HrmApplication).provideAppComponent()

@Module( subcomponents = arrayOf(ActivityComponent::class))
class AppModule(val app: HrmApplication) {
    @Provides
    @AppScope
    fun provideApp(): Application = app

    @Provides
    @AppScope
    fun provideAppContext(): Context = app


    @Provides
    @AppScope
    @OnUiThread
    fun provideUiScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Provides
    @AppScope
    @OnUiThread
    fun provideUiLooper(): Looper = Looper.getMainLooper()

    @Provides
    @AppScope
    @OnStateThread
    fun provideStateLooper(): Looper = Looper.getMainLooper()
}

@Component(modules = arrayOf(AppModule::class, StateModule::class))
@AppScope
interface AppComponent {
    fun provideActBuild(): ActivityComponent.Builder
}