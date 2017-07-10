package com.lukevanoort.hrm.business.state

import android.bluetooth.BluetoothDevice
import android.content.Context
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.lukevanoort.hrm.AppContext
import com.lukevanoort.hrm.AppScope
import io.reactivex.Observable
import javax.inject.Inject

data class RecentDevicesState(val devices: Collection<BluetoothDevice>)

@AppScope
class RecentDevicesManager @Inject constructor(@AppContext val ctx: Context) {
    val prefs = ctx.getSharedPreferences(RecentDevicesManager.Companion.PREFS_KEY, Context.MODE_PRIVATE)
    val rxPrefs = RxSharedPreferences.create(prefs)

    fun asObservable() : Observable<RecentDevicesState> {
        return Observable.empty()
    }

    companion object {
        val PREFS_KEY = "RECENT_DEVICES"
        val LIST_KEY = "DEVICE_LIST"
    }
}