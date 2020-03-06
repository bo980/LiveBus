package com.liang.live.core

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.support.v4.util.ArrayMap

/**class**/
object LiveBus {

//    companion object {
//        val instances by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { LiveBus() }
//    }

    val dispatchers by lazy {
        ArrayMap<String, LiveDispatcher<*>>()
    }

    inline fun <reified T> post(
        value: T,
        delay: Long = 0L,
        tag: String = T::class.java.simpleName
    ): LiveDispatcher<T> {
        synchronized(this) {
            return (dispatchers.getOrPut(
                tag,
                { LiveDispatcher<T>(tag) }) as LiveDispatcher<T>).apply {
                post(value, delay)
            }
        }
    }

    inline fun <reified T> observer(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<T>,
        tag: String = T::class.java.simpleName,
        threadMode: ThreadMode = ThreadMode.MAIN
    ): Subscriber<T> {
        synchronized(this) {
            return (dispatchers.getOrPut(
                tag,
                { LiveDispatcher<T>(tag) }) as LiveDispatcher<T>).with(lifecycleOwner, threadMode)
                .apply {
                    this.observer = observer
                }
        }
    }
}