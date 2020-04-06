package io.reactivex.network

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * 获取 Api 实例
 * [RxNetWork.observable]
 */
fun <T> Class<T>.create(): T = RxNetWork.observable(this)

/**
 * 取消网络请求
 * or
 * [DisposableObserver.isDisposed]
 * [DisposableObserver.dispose]
 */
fun <T> Observable<T>.cancel(tag: Any) = also { RxNetWork.instance.cancel(tag) }

/**
 *
 * [Observable]
 *
 * 网络请求
 */
fun <T> Observable<T>.request(tag: Any, rxNetWorkListenerKt: RxRequestCallbackDSL<T>.() -> Unit) {
    request(tag, RxRequestCallbackDSL<T>().also(rxNetWorkListenerKt).build())
}

/**
 *
 * [Observable]
 *
 * 网络请求
 */
fun <T> Observable<T>.request(tag: Any, callback: RxRequestCallback<T>?) {
    callback?.onNetWorkStart()
    val disposableObserver = object : DisposableObserver<T>() {
        override fun onComplete() {
            callback?.onNetWorkComplete()
        }

        override fun onNext(t: T) {
            callback?.onNetWorkSuccess(t)
        }

        override fun onError(e: Throwable) {
            callback?.onNetWorkError(e)
        }
    }
    RxNetWork.instance.addTag(tag, subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(disposableObserver))
}