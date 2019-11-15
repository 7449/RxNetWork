package com.rxnetwork.sample

import io.reactivex.jsoup.JsoupService
import io.reactivex.jsoup.jsoupApi
import io.reactivex.network.RxNetWork

object JsoupSample {


    fun test() {
        RxNetWork
                .observable(JsoupService::class.java)
                .get("https://www.baoidu.com")
                .jsoupApi<String>("tag") {
                    onNetWorkStart { }
                    onNetWorkSuccess { }
                    onNetWorkComplete { }
                    onNetWorkError { }
                    jsoupRule { document -> document.toString() }
                }
    }

}