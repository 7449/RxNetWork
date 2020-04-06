package com.rxnetwork.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rxnetwork.sample.bus.A
import io.reactivex.bus.RxBus
import io.reactivex.network.RxNetWork
import io.reactivex.network.cancel
import io.reactivex.network.create
import io.reactivex.network.request
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        JsoupSample.test()
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        adapter = MainAdapter()
        recyclerView.adapter = adapter
        btn_test_bus.setOnClickListener(this)
        btn_start_network.setOnClickListener(this)
        btn_cancel_network.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_test_bus -> startActivity(A::class.java)
            R.id.btn_start_network -> {
                Api.ZLService::class.java.create()
                        .getList()
                        .cancel(javaClass.simpleName)
                        .request(javaClass.simpleName) {
                            onNetWorkSuccess { adapter.addAll(it.stories) }
                            onNetWorkComplete { progress.visibility = View.GONE }
                            onNetWorkError { progress.visibility = View.GONE }
                            onNetWorkStart {
                                adapter.clear()
                                progress.visibility = View.VISIBLE
                            }
                        }
            }
            R.id.btn_cancel_network -> RxNetWork.instance.cancel(javaClass.simpleName)
        }
    }

    private fun startActivity(clz: Class<*>) {
        val intent = Intent(applicationContext, clz)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        RxBus.instance.unregisterAll()
    }
}
