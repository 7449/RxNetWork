package io.reactivex.network.bus

/**
 * by y on 2017/5/22
 */

abstract class SimpleRxBusCallBack<T> : RxBusCallBack<T> {
    override fun onBusNext(entity: T) {
    }

    override fun onBusError(throwable: Throwable) {
    }
}
