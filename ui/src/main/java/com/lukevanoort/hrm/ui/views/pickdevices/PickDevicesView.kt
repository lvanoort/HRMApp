package com.lukevanoort.hrm.ui.views.pickdevices

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.support.annotation.StringRes
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.lukevanoort.hrm.app.act.R
import com.lukevanoort.hrm.business.state.ActiveDevicesController
import com.lukevanoort.hrm.business.state.ActiveDevicesState
import com.lukevanoort.hrm.business.state.SingleDeviceState
import com.lukevanoort.hrm.model.HrmDeviceRecord
import com.lukevanoort.hrm.ui.views.inflateChildren
import com.lukevanoort.hrm.ui.views.tintDrawableResource
import io.reactivex.Observable
import org.jetbrains.anko.textResource
import javax.inject.Inject

data class PickDevicesViewState(val activeDeices: Collection<SingleDeviceState>,
                                val nearbyDevices: Collection<HrmDeviceRecord>,
                                val recentDevices: Collection<HrmDeviceRecord>)

interface PickDevicesViewModel {
    fun getState(): Observable<PickDevicesViewState>
    fun activateDevice(device: HrmDeviceRecord)
    fun deactivateDevice(device: HrmDeviceRecord)
}

class PickDeviceViewModelImpl @Inject constructor(
        val activeState: Observable<ActiveDevicesState>,
        val activeController: ActiveDevicesController) : PickDevicesViewModel {

    override fun getState(): Observable<PickDevicesViewState> {
        // TODO: Link this into actual business logic
        return Observable.just(PickDevicesViewState(listOf(), listOf(), listOf()))
    }

    override fun activateDevice(device: HrmDeviceRecord) {
        activeController.requestActivateDevice(device)
    }

    override fun deactivateDevice(device: HrmDeviceRecord) {
        activeController.requestActivateDevice(device)
    }

}

class PickDevicesView : RecyclerView {
    constructor(context: Context) : super(context) {
        initialize(context)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        initialize(context)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initialize(context)
    }

    fun initialize(context: Context) {
        layoutManager = LinearLayoutManager(context)
        adapter = PickDevicesAdapter()
    }
}

private sealed class PickDeviceRow {
    data class RecentDevice(val device: BluetoothDevice) : PickDeviceRow(){
        fun getName() = device.name
    }
    data class ActiveDevice(val device: BluetoothDevice) : PickDeviceRow() {
        fun getName() = device.name
    }
    data class NearbyDevice(val device: BluetoothDevice) : PickDeviceRow() {
        fun getName() = device.name
    }
    data class Heading(@StringRes val textRes: Int) : PickDeviceRow()
}

private class PickDevicesAdapter : ListDelegationAdapter<List<PickDeviceRow>>() {
    init {
        delegatesManager.addDelegate(HeadingAdapter())
        delegatesManager.addDelegate(ActiveDeviceAdapter())
        delegatesManager.addDelegate(RecentDeviceAdapter())
        delegatesManager.addDelegate(NearbyDeviceAdapter())
    }
    private class Differ(val newItems: List<PickDeviceRow>, val oldItems: List<PickDeviceRow>?) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val newItem = newItems[newItemPosition]
            val oldItem = oldItems?.get(oldItemPosition) ?: return false

            if(newItem::class != oldItem::class) {
                return false
            } else {
                when(newItem) {
                    is PickDeviceRow.RecentDevice -> {
                        val oldDev = oldItem as PickDeviceRow.RecentDevice
                        return newItem.device == oldDev.device
                    }
                    is PickDeviceRow.ActiveDevice -> {
                        val oldDev = oldItem as PickDeviceRow.ActiveDevice
                        return newItem.device == oldDev.device
                    }
                    is PickDeviceRow.NearbyDevice -> {
                        val oldDev = oldItem as PickDeviceRow.NearbyDevice
                        return newItem.device == oldDev.device
                    }
                    else -> {
                        return newItem == oldItem
                    }
                }
            }

        }

        override fun getOldListSize(): Int = oldItems?.size ?: 0

        override fun getNewListSize(): Int = newItems.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val newItem = newItems[newItemPosition]
            val oldItem = oldItems?.get(oldItemPosition) ?: return false

            // Lots of this is redundant, but leaving the skeleton here
            // since I think I might want it later
            if(newItem::class != oldItem::class) {
                return false
            } else {
                when(newItem) {
                    is PickDeviceRow.RecentDevice -> {
                        return newItem == oldItem
                    }
                    is PickDeviceRow.ActiveDevice -> {
                        return newItem == oldItem
                    }
                    is PickDeviceRow.NearbyDevice -> {
                        return newItem == oldItem
                    }
                    else -> {
                        return newItem == oldItem
                    }
                }
            }

        }

    }

    override fun setItems(newItems: List<PickDeviceRow>) {
        val result = DiffUtil.calculateDiff(Differ(newItems, items))
        items = newItems
        result.dispatchUpdatesTo(this)
    }
}

private class HeadingAdapter : AbsListItemAdapterDelegate<PickDeviceRow.Heading, PickDeviceRow, HeadingAdapter.VH>() {
    override fun onBindViewHolder(item: PickDeviceRow.Heading, viewHolder: VH, payloads: MutableList<Any>) {
        viewHolder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup): VH = VH(parent.inflateChildren(R.layout.heading_list_item,false))

    override fun isForViewType(item: PickDeviceRow, items: MutableList<PickDeviceRow>, position: Int): Boolean = item is PickDeviceRow.Heading

    private inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById(R.id.tv_title)!! as TextView

        fun bind(item: PickDeviceRow.Heading) {
            title.textResource = item.textRes
        }

    }

}

private class RecentDeviceAdapter : AbsListItemAdapterDelegate<PickDeviceRow.RecentDevice, PickDeviceRow, RecentDeviceAdapter.VH>() {
    override fun onBindViewHolder(item: PickDeviceRow.RecentDevice, viewHolder: VH, payloads: MutableList<Any>) {
        viewHolder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup): VH = VH(parent.inflateChildren(R.layout.device_option_list_item,false))

    override fun isForViewType(item: PickDeviceRow, items: MutableList<PickDeviceRow>, position: Int): Boolean = item is PickDeviceRow.RecentDevice

    private inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById(R.id.tv_title)!! as TextView
        private val image = itemView.findViewById(R.id.iv_icon)!! as ImageView

        init {
            image.setImageDrawable(itemView.context.tintDrawableResource(R.drawable.ic_heart_outline_black_24dp,R.color.colorAccent))
        }

        fun bind(item: PickDeviceRow.RecentDevice) {
            title.text = item.getName()
        }

    }

}

private class ActiveDeviceAdapter : AbsListItemAdapterDelegate<PickDeviceRow.ActiveDevice, PickDeviceRow, ActiveDeviceAdapter.VH>() {
    override fun onBindViewHolder(item: PickDeviceRow.ActiveDevice, viewHolder: VH, payloads: MutableList<Any>) {
        viewHolder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup): VH = VH(parent.inflateChildren(R.layout.active_device_option_list_item,false))

    override fun isForViewType(item: PickDeviceRow, items: MutableList<PickDeviceRow>, position: Int): Boolean = item is PickDeviceRow.ActiveDevice

    private inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById(R.id.tv_title)!! as TextView
        private val image = itemView.findViewById(R.id.iv_icon)!! as ImageView
        private val remove = itemView.findViewById(R.id.ib_remove)!! as ImageButton

        init {
            image.setImageDrawable(itemView.context.tintDrawableResource(R.drawable.ic_heart_solid_black_24dp,R.color.colorAccent))
        }

        fun bind(item: PickDeviceRow.ActiveDevice) {
            title.text = item.getName()
        }

    }
}


private class NearbyDeviceAdapter : AbsListItemAdapterDelegate<PickDeviceRow.NearbyDevice, PickDeviceRow, NearbyDeviceAdapter.VH>() {
    override fun onBindViewHolder(item: PickDeviceRow.NearbyDevice, viewHolder: VH, payloads: MutableList<Any>) {
        viewHolder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup): VH = VH(parent.inflateChildren(R.layout.device_option_list_item,false))

    override fun isForViewType(item: PickDeviceRow, items: MutableList<PickDeviceRow>, position: Int): Boolean = item is PickDeviceRow.ActiveDevice

    private inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById(R.id.tv_title)!! as TextView
        private val image = itemView.findViewById(R.id.iv_icon)!! as ImageView

        init {
            image.setImageResource(R.drawable.ic_bluetooth_black_24dp)
        }

        fun bind(item: PickDeviceRow.NearbyDevice) {
            title.text = item.getName()
        }

    }

}