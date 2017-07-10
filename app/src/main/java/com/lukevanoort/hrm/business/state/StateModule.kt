package com.lukevanoort.hrm.business.state

import com.lukevanoort.hrm.business.state.ActiveDevicesController
import com.lukevanoort.hrm.business.state.ActiveDevicesStateEducer
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class StateModule {
    @Provides
    fun provideActiveDeviceObs(educer: ActiveDevicesStateEducer) = educer.asObservable()

    @Provides
    @Reusable
    fun proviceActiveDeviceController(educer: ActiveDevicesStateEducer): ActiveDevicesController = educer
}