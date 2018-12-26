package com.rxnetwork.sample.samplebus

import android.os.Bundle
import com.rxnetwork.sample.R
import io.reactivex.network.RxBus
import kotlinx.android.synthetic.main.activity_e.*
/**
 * by y on 2017/5/22
 */

class E : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_e)
        A.setOnClickListener { RxBus.instance.post("A", "A activity接收到消息了") }
        B.setOnClickListener { RxBus.instance.post("B", "B activity接收到消息了") }
        C.setOnClickListener { RxBus.instance.post("C", "C activity接收到消息了") }
        D.setOnClickListener { RxBus.instance.post("D", "D activity接收到消息了") }
    }

    override fun onBusNext(entity: String) {
    }
}
