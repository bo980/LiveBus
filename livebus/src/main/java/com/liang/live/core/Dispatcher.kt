package com.liang.live.core

abstract class Dispatcher<T>(val tag: String) {
    abstract fun post(value: T, delay: Long)
}