@file:Suppress("NOTHING_TO_INLINE")

package io.reactivex.network

import io.reactivex.Observable

inline fun <T> Observable<T>.getApi(tag: Any, noinline rxNetWorkListenerKt: SimpleRxNetWorkListenerKt<T>.() -> Unit) {
    RxNetWork.getApi(tag, this, rxNetWorkListenerKt)
}

inline fun <T> Observable<T>.getApi(tag: Any, rxNetWorkListener: RxNetWorkListener<T>) {
    RxNetWork.instance.getApi(tag, this, rxNetWorkListener)
}

inline fun <T> Observable<T>.cancelTag(tag: Any) = also { RxNetWork.cancelTag(tag) }
