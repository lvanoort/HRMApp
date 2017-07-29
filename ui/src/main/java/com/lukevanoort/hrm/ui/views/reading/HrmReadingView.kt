package com.lukevanoort.hrm.ui.views.reading

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import com.lukevanoort.hrm.app.act.R
import com.lukevanoort.hrm.dagger.OnUiThread
import com.lukevanoort.hrm.ui.views.getActivityComponent
import com.lukevanoort.hrm.ui.views.inflateChildren
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.hrmreading_singlehrm_view.view.*
import java.util.*
import javax.inject.Inject

data class HrmReadingViewState (val heartate: Int?)

interface HrmViewModel {
    fun getState() : Observable<HrmReadingViewState>
}

class HrmReadingView : ConstraintLayout {

    @Inject
    lateinit var vm: HrmViewModel
    var disposable: Disposable? = null
    var state = HrmReadingViewState(null)
    set(value) {
        field = value
        reset()
    }


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
        inflateChildren(R.layout.hrmreading_singlehrm_view)
        getActivityComponent()?.inject(this)
    }

    private fun reset() {
        val hr = state.heartate
        if(hr != null) {
            tv_heartrate.setText(hr.toString())
        } else {
            tv_heartrate.setText(R.string.no_heartrate_placeholder)
        }
    }
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        disposable = vm?.getState()
                ?.subscribe{
                    post {
                        state = it
                        reset()
                    }
                }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposable?.dispose()
    }
}

class HrmViewModelImpl: HrmViewModel {
    private val uiLooper : Looper
    private var rand: Random = Random()

    @Inject
    constructor(@OnUiThread uiLooper: Looper) {
       this.uiLooper = uiLooper
    }
    override fun getState(): Observable<HrmReadingViewState> {
        return Observable.create {
            val handler = Handler(uiLooper)
            var runnable: Runnable? = null
            runnable = Runnable {
                if(!it.isDisposed) {
                    if(rand.nextBoolean() || rand.nextBoolean() || rand.nextBoolean()) {
                        it.onNext(HrmReadingViewState(45 + rand.nextInt(5)))
                    } else {
                        it.onNext(HrmReadingViewState(null))
                    }
                    handler.postDelayed(runnable, 500)
                }
            }
            handler.postDelayed(runnable,500)

        }
    }

}
