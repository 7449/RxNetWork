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
        A.setOnClickListener { RxBus.postBus(com.rxnetwork.sample.samplebus.A::class.java.simpleName, "A activity接收到消息了") }
        B.setOnClickListener { RxBus.postBus(com.rxnetwork.sample.samplebus.B::class.java.simpleName, "B activity接收到消息了") }
        C.setOnClickListener { RxBus.postBus(com.rxnetwork.sample.samplebus.C::class.java.simpleName, "C activity接收到消息了") }
        D.setOnClickListener { RxBus.postBus(com.rxnetwork.sample.samplebus.D::class.java.simpleName, "D activity接收到消息了") }
    }

    override fun onBusNext(entity: String) {
    }
}
