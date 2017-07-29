package com.lukevanoort.hrm.business.state

import android.os.Handler
import android.os.Looper
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable

/**
 * An Educer tracks a piece of reactive application state similar to
 * how a Redux store operates
 *
 * An educer can be extended with methods that submit mutation lambdas
 * to the respective mutate functions, which will then be run on the
 * thread used by the [mutationLooper].
 *
 * @param T the type of the data to track, usually a data class
 * @param initialValue The value to initialize the store with
 * @param mutationLooper the Looper to run all mutation events on
 */
open class Educer<T> constructor(initialValue: T, mutationLooper: Looper) {
    private val mutationHandler = Handler(mutationLooper)
    private val relay: BehaviorRelay<T> = BehaviorRelay.createDefault(initialValue)

    private var currentValue: T = initialValue

    /**
     * Mutate the current state
     *
     * This method takes a lambda that receives the current state and returns the new state.
     * This new state will then be set and broadcast to all observers regardless of whether
     * or not it is identical
     *
     * @param mutator The operation to apply to the current state
     */
    protected fun mutate(mutator: (current:T) -> T) {
        mutationHandler.post({
            val changed = mutator(currentValue)
            currentValue = changed
            relay.accept(changed)
        })

    }

    /**
     * Mutate the current state in a manner that may or may not result in a change
     *
     * This method takes a lambda that receives the current state and returns a pair
     * containing first a Boolean indicating whether or not a change did take place,
     * and the second value should be new value to set if the mutation did take place
     *
     * @param mutator The operation to apply to the current state
     */
    protected fun conditionalMutate(mutator: (current: T) -> Pair<Boolean,T>) {
        mutationHandler.post({
            val result = mutator(currentValue)
            if(result.first) {
                currentValue = result.second
                relay.accept(result.second)
            }
        })
    }

    /**
     * Get an observable of the educer's current state
     */
    fun asObservable(): Observable<T> = relay
}