package io.reactivex.bus

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class RxBus private constructor() {

    companion object {
        val instance: RxBus by lazy { RxBus() }
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

    fun containsTag(key: Any): Boolean = rxBusEventArrayMap.containsKey(key)

    /**
     * 取消订阅
     */
    fun unregister(tag: Any) {
        rxBusEventArrayMap[tag]?.let {
            val disposable = it.disposable
            if (!disposable.isDisposed) {
                disposable.dispose()
            }
        }
        rxBusEventArrayMap.remove(tag)
    }

    /**
     * 取消所有订阅
     */
    fun unregisterAll() {
        rxBusEventArrayMap.forEach {
            if (!it.value.disposable.isDisposed) {
                it.value.disposable.dispose()
            }
        }
        rxBusEventArrayMap.clear()
    }

}