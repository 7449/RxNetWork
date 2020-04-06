package com.rxnetwork.sample

import android.util.Log
import io.reactivex.jsoup.JsoupService
import io.reactivex.network.cancel
import io.reactivex.network.request

object JsoupSample {

    fun test() {
        JsoupService
                .createGET("https://www.baidu.com")
                .cancel("tag")
                .request("tag") {
                    onNetWorkError {
                        Log.e("jsoup", it.message.toString())
                    }
                    onNetWorkSuccess {
                        Log.i("jsoup", it.string())
                    }
                }
    }

}