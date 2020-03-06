package com.liang.live.core

import android.annotation.SuppressLint
import android.arch.core.executor.ArchTaskExecutor
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent


class LiveSubscriber<T>(tag: String, threadMode: ThreadMode) : Subscriber<T>(tag, threadMode),
    LifecycleObserver {

    private var mLifecycle: Lifecycle? = null

    @Volatile
    private var mData: T? = null

    override fun bindLifecycle(lifecycle: Lifecycle) {
        mLifecycle = lifecycle.apply {
            addObserver(this@LiveSubscriber)
        }
    }

    override fun unBindLifecycle(unit: () -> Unit) {
        mLifecycle?.removeObserver(this)
        unit()
    }

    @SuppressLint("RestrictedApi")
    override fun postValue(value: T) {
        mData = value
        if (threadMode == ThreadMode.MAIN) {
            ArchTaskExecutor.getInstance().postToMainThread {
                onChange()
            }
        } else onChange()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        enable = true
        onChange()
    }

    private fun onChange() {
        if (enable) {
            observer?.onChanged(mData)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        enable = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        unBindLifecycle {
            LiveBus.dispatchers[tag]?.let {
                (it as LiveDispatcher<T>).removeSubscriber(this)
            }
        }
    }

}