package com.liang.live.core

import android.arch.lifecycle.Observer
import com.liang.live.utils.BACKGROUND

class LiveData<T> {

    @Volatile
    var value: T? = null

    private val mStartVersion = -1
    private var mVersion = mStartVersion
    private var mObserver: Observer<T>? = null

    fun asyncSend(value: T, delay: Long) {
        mVersion++
        this.value = value
        BACKGROUND.submit(ValueRunnable(this.value, delay, mVersion))
    }

    fun observeForever(observer: Observer<T>) {
        mObserver = observer
    }

    private inner class ValueRunnable(val value: T?, val delay: Long, val version: Int) : Runnable {

        override fun run() {
            value ?: return
            Thread.sleep(delay)
            if (mVersion > version || mVersion == mStartVersion) {
                return
            }
            mObserver?.onChanged(value)
        }

    }
}