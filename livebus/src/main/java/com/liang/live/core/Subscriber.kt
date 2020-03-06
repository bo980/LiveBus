package com.liang.live.core

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer


abstract class Subscriber<T>(val tag: String, val threadMode: ThreadMode) {

    protected var enable = false

    var observer: Observer<T>? = null

    abstract fun bindLifecycle(lifecycle: Lifecycle)

    abstract fun unBindLifecycle(unit: () -> Unit)

    abstract fun postValue(value: T)
}