package com.rxnetwork.sample.samplebus

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
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

    override fun onBusNext(entity: String) {
        Toast.makeText(applicationContext, javaClass.simpleName + " : : : " + entity, Toast.LENGTH_SHORT).show()
    }
}
