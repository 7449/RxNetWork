package com.rxnetwork.sample.samplebus

import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import android.widget.Toast
import com.rxnetwork.sample.R

/**
 * by y on 2017/5/22
 */

class A : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)
        findViewById<AppCompatButton>(R.id.btn_simple).setOnClickListener { startActivity(B::class.java) }
    }

    override fun onBusNext(t: String) {
        Toast.makeText(applicationContext, javaClass.simpleName + " : : : " + t, Toast.LENGTH_SHORT).show()
    }
}
