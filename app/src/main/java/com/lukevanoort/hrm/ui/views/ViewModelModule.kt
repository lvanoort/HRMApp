package com.lukevanoort.hrm.ui.views

import com.lukevanoort.hrm.ui.views.reading.HrmViewModel
import com.lukevanoort.hrm.ui.views.reading.HrmViewModelImpl
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {
    @Provides
    fun provideHrmViewModel(impl: HrmViewModelImpl) : HrmViewModel = impl
}
