package io.reactivex.bus

interface RxBusCallBack<T> {
    fun onBusNext(entity: T)

    fun onBusError(throwable: Throwable) = Unit

    fun busOfType(): Class<T>
}