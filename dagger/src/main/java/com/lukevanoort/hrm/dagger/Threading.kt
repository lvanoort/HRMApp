package com.lukevanoort.hrm.dagger

import javax.inject.Qualifier

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class OnUiThread

@Qualifier
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class OnStateThread