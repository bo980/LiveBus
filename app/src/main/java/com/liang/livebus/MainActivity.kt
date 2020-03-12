package com.liang.livebus

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.liang.live.core.LiveBus
import com.liang.live.core.ThreadMode
import com.liang.live.postOverall

import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private val executors = Executors.newSingleThreadExecutor()
    private var started = false
    private var progresss = 0
    private var status = "等待中..."
    private val downLoad = DownLoad(DownLoad.waiting, 0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //默认在主线程中接收
        LiveBus.observer(this, Observer<DownLoad> {
            it ?: return@Observer
            status = when (it.status) {
                DownLoad.downLoading -> "正在下载..."
                DownLoad.finished -> "下载完成"
                else -> "等待中..."
            }
            textView.text = "${status}$it"
            button.text = status
        }, threadMode = ThreadMode.MAIN)
    }

    fun send(view: View) {
        if (started) {
            started = false
            downLoad.status = DownLoad.waiting
            return
        }
        started = true
        executors.submit {
            while (started && progresss < 100) {
                progresss++
                downLoad.status = DownLoad.downLoading
                downLoad.progress = progresss
                Thread.sleep(300)
                //发送消息
                LiveBus.post(downLoad)
            }
            downLoad.status = if (progresss == 100) DownLoad.finished else DownLoad.waiting
            started = false
            //发送消息KTX
            downLoad.postOverall()
            if (progresss == 100) progresss = 0
        }
    }

    fun next(view: View) {
        startActivity(Intent(this, TestActivity::class.java))
    }
}
