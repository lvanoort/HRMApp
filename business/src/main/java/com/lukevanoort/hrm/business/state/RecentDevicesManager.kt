package com.lukevanoort.hrm.business.state

import android.content.Context
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.lukevanoort.hrm.dagger.AppContext
import com.lukevanoort.hrm.dagger.AppScope
import com.lukevanoort.hrm.model.HrmDeviceRecord
import com.squareup.moshi.JsonAdapter
import io.reactivex.Observable
import java.io.IOException
import javax.inject.Inject

data class RecentDevicesState(val devices: Collection<HrmDeviceRecord>)

interface RecentDevicesController {
    fun appendDevice(device: HrmDeviceRecord)
}

@AppScope
class RecentDevicesManager @Inject constructor(
        @AppContext val ctx: Context,
        val deviceListAdapter: JsonAdapter<List<HrmDeviceRecord>>) : RecentDevicesController {

    val prefs = ctx.getSharedPreferences(RecentDevicesManager.Companion.PREFS_KEY, Context.MODE_PRIVATE)
    val rxPrefs = RxSharedPreferences.create(prefs)
    val listPref = rxPrefs.getString(LIST_KEY,"")

    fun asObservable() : Observable<RecentDevicesState> {
        return listPref.asObservable()
                .map {
                    if(it != "") {
                        try {
                            deviceListAdapter.fromJson(it) ?: listOf()
                        } catch (e : IOException) {
                            listOf<HrmDeviceRecord>()
                        }
                    } else {
                        listOf<HrmDeviceRecord>()
                    }
                }
                .map {
                    RecentDevicesState(it as Collection<HrmDeviceRecord>)
                }
    }

    override fun appendDevice(device: HrmDeviceRecord) {
        // This has potential concurrency issues, but given the small
        // stakes of this getting done wrong, it's not a big deal
        val data = listPref.get()
        val currentDevices: MutableList<HrmDeviceRecord>
        if(data != "") {
            currentDevices = deviceListAdapter.fromJson(data)?.toMutableList() ?: mutableListOf()
        } else {
            currentDevices = mutableListOf<HrmDeviceRecord>()
        }

        currentDevices.add(device)
        if(currentDevices.size > RECENT_SIZE) {
            currentDevices.removeAt(0)
        }

        try {
            val str = deviceListAdapter.toJson(currentDevices.toList())
            listPref.set(str)
        } catch (ignored : IOException) {}
    }

    companion object {
        val PREFS_KEY = "RECENT_DEVICES"
        val LIST_KEY = "DEVICE_LIST"
        val RECENT_SIZE = 3
    }
}