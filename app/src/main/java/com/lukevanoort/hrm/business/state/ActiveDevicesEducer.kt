package com.lukevanoort.hrm.business.state

import android.bluetooth.BluetoothDevice
import android.os.Looper
import com.lukevanoort.hrm.AppScope
import com.lukevanoort.hrm.OnStateThread
import com.lukevanoort.hrm.OnUiThread
import javax.inject.Inject

enum class ConnectionStatus {
    CONNECTED,
    DISCONNECTED,
    CONNECTING,
    ERROR
}

data class SingleDeviceState(val device: BluetoothDevice,
                             val connectionStatus: ConnectionStatus,
                             val lastReading: Int?)

data class ActiveDevicesState(val activeDevices: Map<String, SingleDeviceState>,
                              val requestedActiveDevices: Map<String,BluetoothDevice>)

interface ActiveDevicesController {
    fun requestActivateDevice(device: BluetoothDevice)
    fun requestDeactivateDevice(device: BluetoothDevice)
}

@AppScope
class ActiveDevicesStateEducer : Educer<ActiveDevicesState>, ActiveDevicesController {

    constructor(initialValue: ActiveDevicesState, mutationLooper: Looper) : super(initialValue, mutationLooper)
    @Inject
    constructor(@OnStateThread mutationLooper: Looper) : super(ActiveDevicesState(mapOf<String, SingleDeviceState>(), mapOf<String, BluetoothDevice>()), mutationLooper)

    override fun requestActivateDevice(device: BluetoothDevice) = conditionalMutate {
        if(it.requestedActiveDevices.containsKey(device.address)) {
            Pair(false,it)
        } else {
            val mutMap = it.requestedActiveDevices.toMutableMap()
            mutMap.put(device.address,device)
            val newState = it.copy(requestedActiveDevices = mutMap.toMap())
            Pair(true,newState)
        }
    }

    override fun requestDeactivateDevice(device: BluetoothDevice)  = conditionalMutate {
        if(it.requestedActiveDevices.containsKey(device.address)) {
            val mutMap = it.requestedActiveDevices.toMutableMap()
            val oldItem = mutMap.remove(device.address)
            val newState = it.copy(requestedActiveDevices = mutMap.toMap())
            Pair(true,newState)
        } else {
            Pair(false,it)
        }

    }

}