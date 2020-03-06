package com.liang.live


import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import com.liang.live.core.LiveBus
import com.liang.live.core.LiveDispatcher
import com.liang.live.core.Subscriber
import com.liang.live.core.ThreadMode

inline fun <reified T> T.postOverall(
    tag: String = T::class.java.simpleName,
    delay: Long = 0L
): LiveDispatcher<T> {
    return LiveBus.post(this, delay, tag)
}

inline fun <reified T> Observer<T>.changeFromOverall(
    lifecycleOwner: LifecycleOwner,
    tag: String = T::class.java.simpleName,
    threadMode: ThreadMode = ThreadMode.MAIN
): Subscriber<T> {
    return LiveBus.observer(lifecycleOwner, this, tag, threadMode)
}

inline fun <reified T> LifecycleOwner.observerFromOverall(
    tag: String = T::class.java.simpleName,
    threadMode: ThreadMode = ThreadMode.MAIN,
    crossinline action: (value: T) -> Unit
): Subscriber<T> {
    return LiveBus.observer(this, Observer {
        it?.let { t ->
            action(t)
        }
    }, tag, threadMode)
}