package com.rxnetwork.sample.samplebus

import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.rxnetwork.sample.R
import io.reactivex.network.bus.RxBus

/**
 * by y on 2017/5/22
 */

class E : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_e)

        findViewById<AppCompatButton>(R.id.A).setOnClickListener { RxBus.instance.post("A", "A activity接收到消息了") }
        findViewById<AppCompatButton>(R.id.B).setOnClickListener { RxBus.instance.post("B", "B activity接收到消息了") }
        findViewById<AppCompatButton>(R.id.C).setOnClickListener { RxBus.instance.post("C", "C activity接收到消息了") }
        findViewById<AppCompatButton>(R.id.D).setOnClickListener { RxBus.instance.post("D", "D activity接收到消息了") }

    }

    override fun onBusNext(t: String) {

    }
}
