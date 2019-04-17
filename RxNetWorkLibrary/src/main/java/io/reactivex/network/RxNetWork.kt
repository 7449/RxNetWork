package io.reactivex.network


import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.getApi(tag: Any, rxNetWorkListenerDLS: SimpleRxNetWorkListenerDLS<T>.() -> Unit) {
    RxNetWork.getApi(tag, this, rxNetWorkListenerDLS)
}

fun <T> Observable<T>.getApi(tag: Any, rxNetWorkListener: RxNetWorkListener<T>) {
    RxNetWork.instance.getApi(tag, this, rxNetWorkListener)
}

class RxNetWork private constructor() {

    companion object {

        @JvmStatic
        val instance: RxNetWork by lazy { RxNetWork() }

        @JvmStatic
        fun initOption(rxNetOptionFactoryDLS: SimpleRxNetOptionFactoryDLS.() -> Unit) = initialization(SimpleRxNetOptionFactoryDLS().also(rxNetOptionFactoryDLS).build())

        @JvmStatic
        fun initialization(rxNetOptionFactory: RxNetOptionFactory) {
            instance.rxNetOptionFactory = rxNetOptionFactory
        }

        @JvmStatic
        fun <T> getApi(tag: Any, observable: Observable<T>, rxNetWorkListenerDLS: SimpleRxNetWorkListenerDLS<T>.() -> Unit) {
            instance.getApi(tag, observable, SimpleRxNetWorkListenerDLS<T>().also(rxNetWorkListenerDLS).build())
        }

        @JvmStatic
        fun cancelX(tag: Any) = instance.cancel(tag)

        @JvmStatic
        fun cancelAllX() = instance.cancelAll()

        @JvmStatic
        fun containsKeyX(key: Any): Boolean = instance.containsKey(key)

        @JvmStatic
        fun <T> observable(service: Class<T>): T = instance.rxNetOptionFactory.retrofit.create(service)
    }

    private val arrayMap: HashMap<Any, Disposable> = HashMap()
    private lateinit var rxNetOptionFactory: RxNetOptionFactory

    fun <M> getApi(tag: Any, observable: Observable<M>, listener: RxNetWorkListener<M>?) {
        listener?.onNetWorkStart()
        arrayMap[tag] = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<M>() {

                    override fun onError(e: Throwable) {
                        listener?.onNetWorkError(e)
                    }

                    override fun onComplete() {
                        listener?.onNetWorkComplete()
                    }

                    override fun onNext(m: M) {
                        listener?.onNetWorkSuccess(m)
                    }
                })
    }

    fun <M> getApiTask(tag: Any, observable: Observable<M>, listener: RxNetWorkTaskListener<RxNetWorkTask<M>>) {
        listener.onNetWorkStart()
        arrayMap[tag] = observable
                .map { t -> RxNetWorkTask(t, listener.tag) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<RxNetWorkTask<M>>() {

                    override fun onNext(mRxNetWorkTask: RxNetWorkTask<M>) {
                        listener.onNetWorkSuccess(mRxNetWorkTask)
                    }

                    override fun onError(e: Throwable) {
                        listener.onNetWorkError(e)
                    }

                    override fun onComplete() {
                        listener.onNetWorkComplete()
                    }
                })
    }

    fun cancel(tag: Any) {
        val disposable = arrayMap[tag]
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
        arrayMap.remove(tag)
    }

    fun cancelAll() {
        for ((key) in arrayMap) {
            cancel(key)
        }
    }

    fun containsKey(key: Any): Boolean = arrayMap.containsKey(key)

    fun getMap(): HashMap<Any, Disposable> = arrayMap
}
