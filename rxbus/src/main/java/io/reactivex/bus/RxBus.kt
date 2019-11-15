package io.reactivex.bus

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class RxBus private constructor() {

    companion object {
        @JvmStatic
        val instance: RxBus by lazy { RxBus() }

        @JvmStatic
        fun <T> register(any: Any, rxBusCallbackKt: SimpleRxBusCallbackKt<T>.() -> Unit) = instance.register(any, SimpleRxBusCallbackKt<T>().also(rxBusCallbackKt).build())

        @JvmStatic
        fun postBus(tag: Any, obj: Any) = instance.post(tag, obj)

        @JvmStatic
        fun unregisterBus(tag: Any) = instance.unregister(tag)

        @JvmStatic
        fun unregisterAllBus() = instance.unregisterAll()

        @JvmStatic
        fun disposeBus(tag: Any): Boolean = instance.dispose(tag)
    }

    private val rxBusEventArrayMap: HashMap<Any, RxBusEvent> = HashMap()

    fun post(tag: Any, obj: Any): Boolean {
        val rxBusEvent = rxBusEventArrayMap[tag] ?: return true
        rxBusEvent.subject.onNext(obj)
        return false
    }

    /**
     * 接受消息
     *
     * @param tag      标志
     * @param callBack 回调
     */
    fun <T> register(tag: Any, callBack: RxBusCallBack<T>) {
        var rxBusEvent = rxBusEventArrayMap[tag]
        if (rxBusEvent == null) {
            val toSerialized = PublishSubject.create<Any>().toSerialized()
            rxBusEvent = RxBusEvent(toSerialized, toSerialized
                    .ofType(callBack.busOfType())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableObserver<T>() {
                        override fun onComplete() {
                        }

                        override fun onError(e: Throwable) {
                            callBack.onBusError(e)
                        }

                        override fun onNext(t: T) {
                            callBack.onBusNext(t)
                        }
                    })
            )
            rxBusEventArrayMap[tag] = rxBusEvent
        }
    }

    /**
     * 取消订阅
     *
     * @param tag 标志
     * @return true 取消成功
     */
    fun unregister(tag: Any): Boolean {
        val rxBusEvent = rxBusEventArrayMap[tag] ?: return true
        val disposable = rxBusEvent.disposable
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        rxBusEventArrayMap.remove(tag)
        return false
    }

    /**
     * 取消所有订阅
     *
     * @return 返回false 证明还有订阅没有被取消,注意内存泄漏...
     */
    fun unregisterAll(): Boolean {
        for ((key) in rxBusEventArrayMap) {
            val unregister = unregister(key)
            if (!unregister) {
                return false
            }
        }
        return true
    }

    fun dispose(tag: Any): Boolean = rxBusEventArrayMap[tag]?.disposable?.isDisposed ?: true
}