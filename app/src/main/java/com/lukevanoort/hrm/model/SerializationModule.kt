package com.lukevanoort.hrm.model

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.Module
import dagger.Provides
import dagger.Reusable



@Module
class SerializationModule {

    @Provides
    @Reusable
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    @Reusable
    fun provideDeviceJsonAdapter(moshi: Moshi): JsonAdapter<HrmDeviceRecord> {
        return moshi.adapter(HrmDeviceRecord::class.java)
    }

    @Provides
    @Reusable
    fun provideDeviceListJsonAdapter(moshi: Moshi): JsonAdapter<List<HrmDeviceRecord>> {
        val type = Types.newParameterizedType(List::class.java, HrmDeviceRecord::class.java)
        return moshi.adapter(type)
    }
}