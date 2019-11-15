package io.reactivex.jsoup

import io.reactivex.Observable
import io.reactivex.network.RxNetWork
import okhttp3.ResponseBody

fun <T> Observable<ResponseBody>.jsoupApi(tag: Any, rxNetWorkListenerKt: SimpleJsoupRxNetWorkListenerKt<T>.() -> Unit) {
    RxNetWork.instance.getApi(tag, this, SimpleJsoupRxNetWorkListenerKt<T>().also(rxNetWorkListenerKt).build())
}