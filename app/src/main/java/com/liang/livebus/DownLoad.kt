package com.liang.livebus

data class DownLoad(var status: Int, var progress: Int) {
    companion object {
        const val waiting = 2
        const val downLoading = 1
        const val finished = 0
    }
}