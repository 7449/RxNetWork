package com.rxnetwork.sample.samplebus

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import io.reactivex.network.RxBus
import io.reactivex.network.RxBusCallBack

/**
 * by y on 2017/5/22
 */

abstract class BaseActivity : AppCompatActivity(), RxBusCallBack<String> {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxBus.instance.register(javaClass.simpleName, this)
    }

    override fun onBusError(throwable: Throwable) {
        Toast.makeText(applicationContext, throwable.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun busOfType(): Class<String> = String::class.java

    override fun onDestroy() {
        super.onDestroy()
        RxBus.instance.unregister(javaClass.simpleName)
    }

    protected fun startActivity(clz: Class<*>) {
        val intent = Intent(applicationContext, clz)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }


}
