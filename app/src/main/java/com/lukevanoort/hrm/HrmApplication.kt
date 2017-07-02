package com.lukevanoort.hrm

import android.app.Application
import android.content.Context
import com.lukevanoort.hrm.ui.ActivityComponent
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

///*
@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class AppContext

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
}

@Component(modules = arrayOf(AppModule::class))
@AppScope
interface AppComponent {
    fun provideActBuild(): ActivityComponent.Builder
}