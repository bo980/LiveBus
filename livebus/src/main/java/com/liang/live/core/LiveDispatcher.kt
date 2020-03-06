package com.liang.live.core


import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import com.liang.live.utils.put


class LiveDispatcher<T>(tag: String) : Dispatcher<T>(tag) {

    private val subscribers by lazy {
        ArrayList<Subscriber<T>>()
    }

    private val mLiveData by lazy {
        LiveData<T>()
    }

    override fun post(value: T, delay: Long) {
        mLiveData.asyncSend(value, delay)
    }

    init {
        mLiveData.observeForever(Observer {
            it?.let { t ->
                subscribers.forEach { s ->
                    s.postValue(t)
                }
            }
        })
    }

    fun with(lifecycleOwner: LifecycleOwner, threadMode: ThreadMode): Subscriber<T> {
        return subscribers.put(LiveSubscriber<T>(tag, threadMode).apply {
            bindLifecycle(lifecycleOwner.lifecycle)
            mLiveData.value?.let {
                postValue(it)
            }
        })
    }

    fun removeSubscriber(subscriber: Subscriber<T>) {
        subscribers.remove(subscriber)
    }
}
