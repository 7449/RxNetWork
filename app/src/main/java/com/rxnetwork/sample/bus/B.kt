package com.rxnetwork.sample.bus

import android.os.Bundle
import com.rxnetwork.sample.R
import io.reactivex.bus.RxBus
import kotlinx.android.synthetic.main.activity_bus.*

/**
 * by y on 2017/5/22
 */

class B : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus)
        btn_bus.setOnClickListener { RxBus.instance.post(A::class.java.simpleName, "A activity接收到消息了") }
    }

    override fun onBusNext(entity: String) {
    }
}
