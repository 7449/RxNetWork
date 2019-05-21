package com.rxnetwork.sample.bus

import android.os.Bundle
import android.widget.Toast
import com.rxnetwork.sample.R
import kotlinx.android.synthetic.main.activity_bus.*

/**
 * by y on 2017/5/22
 */

class A : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus)
        btn_bus.setOnClickListener { startActivity(B::class.java) }
    }

    override fun onBusNext(entity: String) {
        Toast.makeText(applicationContext, javaClass.simpleName + " : : : " + entity, Toast.LENGTH_SHORT).show()
    }
}
