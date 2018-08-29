package io.reactivex.network

/**
 * @author y
 */
interface RxNetWorkTaskListener<T> : RxNetWorkListener<T> {
    val tag: Any
}