package io.reactivex.network

import io.reactivex.Observable

fun <T> Observable<T>.getApi(tag: Any, rxNetWorkListenerKt: SimpleRxNetWorkListenerKt<T>.() -> Unit) {
    RxNetWork.getApi(tag, this, rxNetWorkListenerKt)
}

fun <T> Observable<T>.getApi(tag: Any, rxNetWorkListener: RxNetWorkListener<T>) {
    RxNetWork.instance.getApi(tag, this, rxNetWorkListener)
}

fun <T> Observable<T>.cancelTag(tag: Any) = also { RxNetWork.cancelTag(tag) }