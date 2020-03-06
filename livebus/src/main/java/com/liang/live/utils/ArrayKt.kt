package com.liang.live.utils

import com.liang.live.core.Subscriber


fun <T> ArrayList<Subscriber<T>>.put(subscriber: Subscriber<T>): Subscriber<T> {
    add(0, subscriber)
    return subscriber
}