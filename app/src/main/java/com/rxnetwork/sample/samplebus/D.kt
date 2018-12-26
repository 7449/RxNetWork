package com.rxnetwork.sample.samplebus

import android.os.Bundle
import android.widget.Toast
import com.rxnetwork.sample.R
import kotlinx.android.synthetic.main.activity_simple.*

/**
 * by y on 2017/5/22
 */

class D : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)
        btn_simple.setOnClickListener { startActivity(E::class.java) }
    }

    override fun onBusNext(entity: String) {
        Toast.makeText(applicationContext, javaClass.simpleName + " : : : " + entity, Toast.LENGTH_SHORT).show()
    }
}
