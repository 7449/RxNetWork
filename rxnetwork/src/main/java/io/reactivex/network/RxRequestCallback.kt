package io.reactivex.network

/**
 * 网络回调
 */
interface RxRequestCallback<T> {
    fun onNetWorkStart()

    fun onNetWorkSuccess(data: T)

    fun onNetWorkError(throwable: Throwable)

    fun onNetWorkComplete()
}