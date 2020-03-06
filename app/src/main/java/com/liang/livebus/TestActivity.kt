package com.liang.livebus

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.liang.live.observerFromOverall
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {
    private var status = "等待中..."
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        //KTX
        //默认在主线程中接收observerFromOverall<DownLoad>(threadMode = ThreadMode.MAIN)
        observerFromOverall<DownLoad> {
            status = when (it.status) {
                DownLoad.downLoading -> "正在下载..."
                DownLoad.finished -> "下载完成"
                else -> "等待中..."
            }
            textView.text = "${status}$it"
            progressBar.progress = it.progress
        }

        /*
         另一种接收方式
         Observer<DownLoad>
            status = when (it.status) {
                DownLoad.downLoading -> "正在下载..."
                DownLoad.finished -> "下载完成"
                else -> "等待中..."
            }
            textView.text = "${status}$it"
            progressBar.progress = it.progress
         }.changeFromOverall(lifecycleOwner = this)
         */


    }
}