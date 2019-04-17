package io.reactivex.network

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class RxBus private constructor() {

    companion object {
        @JvmStatic
        internal val instance: RxBus by lazy { RxBus() }

        @JvmStatic
        fun <T> register(any: Any, rxBusCallbackDLS: SimpleRxBusCallbackDLS<T>.() -> Unit) = instance.register(any, SimpleRxBusCallbackDLS<T>().also(rxBusCallbackDLS).build())

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
            rxBusEvent = RxBusEvent()
            rxBusEvent.subject = PublishSubject.create<Any>().toSerialized()
            rxBusEvent.disposable = rxBusEvent.subject
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

    fun dispose(tag: Any): Boolean {
        return rxBusEventArrayMap[tag]?.disposable?.isDisposed ?: true
    }

}

class RxBusEvent {
    lateinit var subject: Subject<Any>
    lateinit var disposable: DisposableObserver<*>
}


interface RxBusCallBack<T> {
    fun onBusNext(entity: T)

    fun onBusError(throwable: Throwable) = Unit

    fun busOfType(): Class<T> = javaClass.genericSuperclass as Class<T>
}

class SimpleRxBusCallbackDLS<T> {
    private var onBusNext: ((entity: T) -> Unit)? = null
    private var onBusError: ((throwable: Throwable) -> Unit)? = null
    private var busOfType: (() -> Class<T>)? = null

    fun onBusNext(onBusNext: (entity: T) -> Unit) {
        this.onBusNext = onBusNext
    }

    fun onBusError(onBusError: (throwable: Throwable) -> Unit) {
        this.onBusError = onBusError
    }

    fun busOfType(busOfType: () -> Class<T>) {
        this.busOfType = busOfType
    }

    internal fun build(): RxBusCallBack<T> {
        return object : RxBusCallBack<T> {
            override fun onBusNext(entity: T) {
                onBusNext?.invoke(entity)
            }

            override fun onBusError(throwable: Throwable) {
                onBusError?.invoke(throwable)
            }

            override fun busOfType(): Class<T> {
                return busOfType?.invoke() ?: super.busOfType()
            }
        }
    }
}
