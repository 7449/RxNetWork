package com.rxnetwork.sample.bus

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.bus.RxBus
import io.reactivex.bus.RxBusCallBack

/**
 * by y on 2017/5/22
 */

abstract class BaseActivity : AppCompatActivity(), RxBusCallBack<String> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxBus.register<Any>(javaClass.simpleName) {
            onBusNext { Toast.makeText(applicationContext, javaClass.simpleName + " : : : " + it, Toast.LENGTH_SHORT).show() }
            busOfType { Any::class.java }
        }
    }

    override fun onBusError(throwable: Throwable) {
        Toast.makeText(applicationContext, throwable.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun busOfType(): Class<String> = String::class.java

    override fun onDestroy() {
        super.onDestroy()
        RxBus.unregisterBus(javaClass.simpleName)
    }

    protected fun startActivity(clz: Class<*>) {
        val intent = Intent(applicationContext, clz)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
