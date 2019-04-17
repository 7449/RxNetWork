package com.rxnetwork.sample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.reflect.TypeToken
import com.rxnetwork.sample.samplebus.A
import com.socks.library.KLog
import io.reactivex.network.RxBus
import io.reactivex.network.RxNetWork
import io.reactivex.network.cache.RxCache
import io.reactivex.network.getApi
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxBus.register<ListModel>(BUS_TAG) { onBusNext { Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_SHORT).show() } }
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        adapter = MainAdapter()
        recyclerView.adapter = adapter
        btn_send.setOnClickListener(this)
        btn_unregister.setOnClickListener(this)
        btn_test_bus.setOnClickListener(this)
        btn_start_network.setOnClickListener(this)
        btn_cancel_network.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_send -> RxBus.postBus(BUS_TAG, ListModel())
            R.id.btn_unregister -> Toast.makeText(applicationContext, RxBus.unregisterBus(BUS_TAG).toString(), Toast.LENGTH_SHORT).show()
            R.id.btn_test_bus -> startActivity(A::class.java)
            R.id.btn_start_network -> {
                RxNetWork
                        .observable(Api.ZLService::class.java)
                        .getList()
                        .compose(RxCache.instance.transformerCN("cache", true, object : TypeToken<ListModel>() {}))
                        .map { listCacheResult ->
                            Log.i("RxCache", listCacheResult.type.toString() + " ------ " + listCacheResult.result)
                            listCacheResult.result
                        }
                        .getApi(javaClass.simpleName) {
                            onNetWorkSuccess { adapter.addAll(it.top_stories) }
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
        bus_message.text = ""
    }

    private fun startActivity(clz: Class<*>) {
        val intent = Intent(applicationContext, clz)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        KLog.i(RxBus.unregisterAllBus())
    }

    companion object {
        private const val BUS_TAG = "bus_tag"
    }
}
